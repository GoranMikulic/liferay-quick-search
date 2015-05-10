package mikugo.dev.search;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.index.IndexSearcherImpl;
import mikugo.dev.search.model.ResultModel;
import mikugo.dev.search.query.DynamicQueryResultFactory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ArrayUtil;

/**
 * This class recognizes the search type by pattern and delegates the search
 * request to the different search implementations
 * 
 * @author mikugo
 *
 */
public class SearchProxy {

    private static Log log = LogFactoryUtil.getLog(SearchProxy.class);

    /**
     * Recognizing pattern and initializing search
     * 
     * @param request
     * @param response
     * @param pattern
     * @param configuredAssetTypes
     * @param maximumSearchResults
     * @return A list of {@link ResultModel}
     */
    public List<ResultModel> search(ResourceRequest request, ResourceResponse response, String pattern,
	    String[] configuredAssetTypes, int maximumSearchResults) {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	if (Utils.isFaceted(pattern)) {

	    resultModelList = doFacetedSearch(request, response, pattern, configuredAssetTypes, maximumSearchResults);

	} else {

	    resultModelList = doIndexSearch(request, response, pattern, Utils.filterIndexTypes(configuredAssetTypes),
		    maximumSearchResults);

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
     * @param maximumSearchResults
     * @param configuredAssetTypes
     * @return A list of {@link ResultModel}
     */
    private List<ResultModel> doFacetedSearch(ResourceRequest request, ResourceResponse response, String pattern,
	    String[] configuredAssetTypes, int maximumSearchResults) {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();
	// Parsing user input for search type and keyword
	String searchType = pattern.substring(0, pattern.indexOf(":"));
	pattern = pattern.substring(pattern.indexOf(":") + 1).trim();

	boolean searchTypeIsContainedInConfiguredTypes = ArrayUtil.contains(
		AssetTypes.getReadableNames(configuredAssetTypes), searchType);

	if (ArrayUtil.contains(AssetTypes.getDynamicQueryReadableNames(), searchType)
		&& searchTypeIsContainedInConfiguredTypes) {

	    resultModelList = doDynamicQuerySearch(request, pattern, searchType, maximumSearchResults);

	} else if (ArrayUtil.contains(AssetTypes.getIndexSearchReadableNames(), searchType)
		&& searchTypeIsContainedInConfiguredTypes) {

	    resultModelList = doIndexSearch(request, response, pattern,
		    new String[] { AssetTypes.getClassName(searchType) }, maximumSearchResults);
	}

	return resultModelList;
    }

    /**
     * Processing a Dynamic Query Search
     * 
     * @param request
     * @param pattern
     * @param maximumSearchResults
     * @param resultModelList
     * @param searchType
     * @return A list of {@link ResultModel}
     */
    private List<ResultModel> doDynamicQuerySearch(ResourceRequest request, String pattern, String searchType,
	    int maximumSearchResults) {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	try {
	    resultModelList = new DynamicQueryResultFactory(AssetTypes.getClassName(searchType), maximumSearchResults,
		    Utils.getThemeDisplay(request), pattern).getResult();
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
     * @param maximumSearchResults
     * @param resultModelList
     * @return A list of {@link ResultModel}
     */
    private List<ResultModel> doIndexSearch(ResourceRequest request, ResourceResponse response, String pattern,
	    String[] assetTypes, int maximumSearchResults) {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	try {
	    resultModelList = new IndexSearcherImpl(request, pattern, assetTypes, maximumSearchResults, response)
		    .getResult();
	} catch (Exception e) {
	    log.error(e);
	}
	return resultModelList;
    }
}
