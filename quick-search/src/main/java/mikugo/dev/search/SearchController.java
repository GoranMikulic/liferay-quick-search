package mikugo.dev.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.index.IndexSearcherImpl;
import mikugo.dev.search.model.ResultModel;
import mikugo.dev.search.query.DynamicQueryResultFactory;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * Portlet-Controller-Class, handles the requests from the UI
 * 
 * @author mikugo
 *
 */
public class SearchController extends MVCPortlet {

    private static Log log = LogFactoryUtil.getLog(SearchController.class);

    private int maximumSearchResults;
    private String[] configuredAssetTypes;

    /**
     * Mapping requests
     */
    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException,
	    PortletException {
	super.serveResource(resourceRequest, resourceResponse);

	if (resourceRequest.getParameter("action").equals("search")) {

	    initSettings(resourceRequest);
	    serveResults(resourceRequest, resourceResponse, resourceRequest.getParameter("pattern"));

	}
    }

    /**
     * Initializing portlet settings
     * 
     * @param request
     */
    private void initSettings(ResourceRequest request) {

	PortletPreferences preferences = request.getPreferences();

	this.maximumSearchResults = GetterUtil.getInteger(preferences.getValue(
		Utils.CONFIGURATION_MAXIMUM_SEARCH_RESULTS, "5"));

	this.configuredAssetTypes = preferences.getValues(Utils.CONFIGURATION_ASSET_TYPES,
		AssetTypes.getAllClassNames());
    }

    /**
     * Initializing search and serving results for UI
     * 
     * @param request
     * @param response
     * @param pattern
     *            - The search pattern
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public void serveResults(ResourceRequest request, ResourceResponse response, String pattern)
	    throws JsonGenerationException, JsonMappingException, IOException {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	if (Utils.isFaceted(pattern)) {
	    resultModelList = doFacetedSearch(request, response, pattern, resultModelList);
	} else {

	    String[] filteredIndexTypes = Utils.filterIndexTypes(configuredAssetTypes);

	    resultModelList = doIndexSearch(request, response, pattern, filteredIndexTypes);
	}

	ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getWriter(), resultModelList);
	response.getWriter().flush();
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
     * @return
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

    /**
     * This method is processing the search, delegates the different search
     * types and returns a list of {@link ResultModel}
     * 
     * @param request
     * @param response
     * @param pattern
     * @param maximumSearchResults
     * @param resultModelList
     * @param configuredAssetTypes
     * @return A list of {@link ResultModel}
     */
    private List<ResultModel> doFacetedSearch(ResourceRequest request, ResourceResponse response, String pattern,
	    List<ResultModel> resultModelList) {

	// Parsing user input for search type and keyword
	String searchType = pattern.substring(0, pattern.indexOf(":"));
	pattern = pattern.substring(pattern.indexOf(":") + 1).trim();

	boolean searchTypeIsContainedInConfiguredTypes = ArrayUtil.contains(
		AssetTypes.getReadableNames(configuredAssetTypes), searchType);

	if (ArrayUtil.contains(AssetTypes.getDynamicQueryReadableNames(), searchType)
		&& searchTypeIsContainedInConfiguredTypes) {

	    resultModelList = doDynamicQuerySearch(request, pattern, resultModelList, searchType);

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
     * @param maximumSearchResults
     * @param resultModelList
     * @param searchType
     * @return A list of {@link ResultModel}
     */
    private List<ResultModel> doDynamicQuerySearch(ResourceRequest request, String pattern,
	    List<ResultModel> resultModelList, String searchType) {
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
     * @return the configuredAssetTypes
     */
    public String[] getConfiguredAssetTypes() {
	return this.configuredAssetTypes;
    }

    /**
     * @return the maximumSearchResults
     */
    public int getMaximumSearchResults() {
	return this.maximumSearchResults;
    }
}
