package mikugo.dev.search;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.combined.CombinedSearchImpl;
import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.index.IndexSearcherImpl;
import mikugo.dev.search.model.ResultModel;
import mikugo.dev.search.query.DynamicQueryFacetedSearchImpl;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ArrayUtil;

/**
 * This class recognizes the search type by pattern and delegates the search
 * request to the different search implementations
 * 
 * @author Goran Mikulic
 *
 */
public class SearchProxy {

    private static Log log = LogFactoryUtil.getLog(SearchProxy.class);

    private SearchSettings settings;
    private ResourceRequest request;
    private ResourceResponse response;
    private String pattern;

    public SearchProxy(SearchSettings settings, ResourceRequest request, ResourceResponse response, String pattern) {
	super();
	this.settings = settings;
	this.request = request;
	this.response = response;
	this.pattern = pattern;
    }

    /**
     * Recognizing pattern and initializing search
     * 
     * @return Returns list of {@link ResultModel}
     */
    public List<ResultModel> search() {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	if (Utils.isFaceted(pattern)) {

	    resultModelList = doFacetedSearch(request, response, pattern);

	} else {

	    try {
		resultModelList = new CombinedSearchImpl(request, pattern, Utils.filterIndexTypes(settings
			.getConfiguredAssetTypes()), settings.getMaximumSearchResults(), response).getResult();
	    } catch (SearchException e) {
		log.error(e);
	    } catch (Exception e) {
		log.error(e);
	    }

	}

	return resultModelList;

    }

    /**
     * This method is processing the search, delegates the different search
     * types and returns a list of {@link ResultModel}
     * 
     * @param request
     * @param response
     * @param pattern
     * @return A list of {@link ResultModel}
     */
    private List<ResultModel> doFacetedSearch(ResourceRequest request, ResourceResponse response, String pattern) {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();
	// Parsing user input for search type and keyword
	String searchType = pattern.substring(0, pattern.indexOf(":"));
	pattern = pattern.substring(pattern.indexOf(":") + 1).trim();

	boolean searchTypeIsContainedInConfiguredTypes = ArrayUtil.contains(
		AssetTypes.getReadableNames(settings.getConfiguredAssetTypes()), searchType);

	if (ArrayUtil.contains(AssetTypes.getDynamicQueryReadableNames(), searchType)
		&& searchTypeIsContainedInConfiguredTypes) {

	    resultModelList = doDynamicQuerySearch(request, pattern, searchType);

	} else if (ArrayUtil.contains(AssetTypes.getIndexSearchReadableNames(), searchType)
		&& searchTypeIsContainedInConfiguredTypes) {

	    resultModelList = doIndexSearch(request, response, pattern,
		    new String[] { AssetTypes.getClassName(searchType) });
	}

	return resultModelList;
    }

    /**
     * Processing a Dynamic Query Search
     * 
     * @param request
     * @param pattern
     * @param searchType
     * @return A list of {@link ResultModel}
     */
    private List<ResultModel> doDynamicQuerySearch(ResourceRequest request, String pattern, String searchType) {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	try {
	    resultModelList = new DynamicQueryFacetedSearchImpl(AssetTypes.getClassName(searchType),
		    settings.getMaximumSearchResults(), Utils.getThemeDisplay(request), pattern).getResult();
	} catch (SearchException e) {
	    log.error(e);
	} catch (Exception e) {
	    log.error(e);
	}
	return resultModelList;
    }

    /**
     * Initializing Index Search without type facets
     * 
     * @param request
     * @param response
     * @param pattern
     * @param assetTypes
     * @return A list of {@link ResultModel}
     */
    private List<ResultModel> doIndexSearch(ResourceRequest request, ResourceResponse response, String pattern,
	    String[] assetTypes) {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	try {
	    resultModelList = new IndexSearcherImpl(request, pattern, assetTypes, settings.getMaximumSearchResults(),
		    response).getResult();
	} catch (Exception e) {
	    log.error(e);
	}
	return resultModelList;
    }
}
