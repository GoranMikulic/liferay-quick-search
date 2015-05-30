package mikugo.dev.search;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.model.ResultModel;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * Portlet-Controller-Class, handles the requests from the UI
 * 
 * @author Goran Mikulic
 *
 */
public class SearchController extends MVCPortlet {

    private SearchSettings searchSettings = new SearchSettings();

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

	searchSettings.setMaximumSearchResults(GetterUtil.getInteger(preferences.getValue(
		Utils.CONFIGURATION_MAXIMUM_SEARCH_RESULTS, "5")));

	searchSettings.setConfiguredAssetTypes(preferences.getValues(Utils.CONFIGURATION_ASSET_TYPES,
		AssetTypes.getAllClassNames()));
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

	List<ResultModel> resultModelList = new SearchProxy(searchSettings, request, response, pattern).search();

	ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getWriter(), resultModelList);
	response.getWriter().flush();
    }

}
