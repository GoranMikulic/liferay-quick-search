package mikugo.dev.search.combined;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ArrayUtil;

import mikugo.dev.search.SearchProxy;
import mikugo.dev.search.data.Search;
import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.index.IndexSearcherImpl;
import mikugo.dev.search.model.ResultModel;
import mikugo.dev.search.query.DynamicQueryFacetedSearchImpl;

public class FacetedSearchImpl implements Search {

    private ResourceRequest request;
    private ResourceResponse response;
    private String pattern;
    private String[] configuredTypes;
    private int maximumSearchResults;

    public FacetedSearchImpl(ResourceRequest request, ResourceResponse response, String pattern,
	    String[] configuredTypes, int maximumSearchResults) {
	this.request = request;
	this.response = response;
	this.pattern = pattern;
	this.configuredTypes = configuredTypes;
	this.maximumSearchResults = maximumSearchResults;
    }

    @Override
    public List<ResultModel> getResult() throws SearchException, Exception {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();
	// Parsing user input for search type and keyword
	String searchType = pattern.substring(0, pattern.indexOf(":"));
	pattern = pattern.substring(pattern.indexOf(":") + 1).trim();

	boolean searchTypeIsContainedInConfiguredTypes = ArrayUtil.contains(
		AssetTypes.getReadableNames(configuredTypes), searchType);

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
		    maximumSearchResults, Utils.getThemeDisplay(request), pattern).getResult();
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
	    resultModelList = new IndexSearcherImpl(request, pattern, assetTypes, maximumSearchResults, response)
		    .getResult();
	} catch (Exception e) {
	    log.error(e);
	}
	return resultModelList;
    }

    private static Log log = LogFactoryUtil.getLog(SearchProxy.class);

}
