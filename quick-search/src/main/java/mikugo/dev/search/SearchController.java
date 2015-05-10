package mikugo.dev.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.data.DynamicQueryResult;
import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.impl.IndexSearcherImpl;
import mikugo.dev.search.model.ResultModel;

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

    /**
     * Mapping requests
     */
    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException,
	    PortletException {
	super.serveResource(resourceRequest, resourceResponse);

	if (resourceRequest.getParameter("action").equals("search")) {

	    serveResults(resourceRequest, resourceResponse, resourceRequest.getParameter("pattern"));

	}
    }

    /**
     * Checks if the search keyword contains the pattern <type>:<keyword>
     * 
     * @param pattern
     * @return
     */
    private boolean isFaceted(String pattern) {
	// Regex should search for following pattern <type>:<keyword>
	Pattern regex = Pattern.compile("([A-Z,a-z])+\\w:((\\s)*([A-Z,a-z])*)*");
	Matcher matcher = regex.matcher(pattern);
	return matcher.matches();
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

	PortletPreferences preferences = request.getPreferences();

	String[] configuredAssetTypes = preferences.getValues(Utils.CONFIGURATION_ASSET_TYPES,
		AssetTypes.getAllClassNames());

	int maximumSearchResults = GetterUtil.getInteger(preferences.getValue(
		Utils.CONFIGURATION_MAXIMUM_SEARCH_RESULTS, "5"));

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	if (isFaceted(pattern)) {
	    resultModelList = doFacetedSearch(request, response, pattern, maximumSearchResults, resultModelList,
		    configuredAssetTypes);
	} else {

	    String[] filteredIndexTypes = filterIndexTypes(configuredAssetTypes);

	    resultModelList = doIndexSearch(request, response, pattern, filteredIndexTypes, maximumSearchResults);
	}

	ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getWriter(), resultModelList);
	response.getWriter().flush();
    }

    /**
     * Removing dynamic query class names from the configured asset types to get
     * the configured types for index search
     * 
     * @param assetTypes
     * 
     * @return
     */
    private String[] filterIndexTypes(String[] assetTypes) {

	List<String> filteredIndexTypes = new ArrayList<String>();

	for (String dynQueryClassName : AssetTypes.getIndexSearchClassNames()) {
	    if (ArrayUtil.contains(assetTypes, dynQueryClassName)) {
		filteredIndexTypes.add(dynQueryClassName);
	    }
	}

	return (String[]) filteredIndexTypes.toArray();
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
	    int maximumSearchResults, List<ResultModel> resultModelList, String[] configuredAssetTypes) {

	// Parsing user input for search type and keyword
	String searchType = pattern.substring(0, pattern.indexOf(":"));
	pattern = pattern.substring(pattern.indexOf(":") + 1).trim();

	if (ArrayUtil.contains(AssetTypes.getDynamicQueryReadableNames(), searchType)
		&& ArrayUtil.contains(AssetTypes.getReadableNames(configuredAssetTypes), searchType)) {

	    resultModelList = doDynamicQuerySearch(request, pattern, maximumSearchResults, resultModelList, searchType);

	} else if (ArrayUtil.contains(AssetTypes.getIndexSearchReadableNames(), searchType)
		&& ArrayUtil.contains(AssetTypes.getReadableNames(configuredAssetTypes), searchType)) {

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
    private List<ResultModel> doDynamicQuerySearch(ResourceRequest request, String pattern, int maximumSearchResults,
	    List<ResultModel> resultModelList, String searchType) {
	try {
	    resultModelList = new DynamicQueryResult(AssetTypes.getClassName(searchType), maximumSearchResults,
		    Utils.getThemeDisplay(request), pattern).getResult();
	} catch (SearchException e) {
	    log.error(e);
	} catch (Exception e) {
	    log.error(e);
	}
	return resultModelList;
    }
}
