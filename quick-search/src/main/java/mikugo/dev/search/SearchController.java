package mikugo.dev.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.data.DynamicQueryResultBuilder;
import mikugo.dev.search.data.IndexSearcherImpl;
import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.helper.Utils;
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

    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException,
	    PortletException {
	super.serveResource(resourceRequest, resourceResponse);

	if (resourceRequest.getParameter("action").equals("search")) {

	    serveResults(resourceRequest, resourceResponse, resourceRequest.getParameter("pattern"));

	}
    }

    public void serveResults(ResourceRequest request, ResourceResponse response, String pattern)
	    throws JsonGenerationException, JsonMappingException, IOException {

	PortletPreferences preferences = request.getPreferences();
	String[] assetTypes = preferences.getValues(Utils.CONFIGURATION_ASSET_TYPES, AssetTypes.getAllClassNames());

	boolean isCustomAssetSearch = false;

	// check if pattern has a facet prefix and manipulate search
	if (pattern.contains(":")) {
	    String prefix = pattern.substring(0, pattern.indexOf(":"));
	    String className = AssetTypes.getClassName(prefix);

	    if (className != null && ArrayUtil.contains(assetTypes, className)) {
		assetTypes = new String[] { className };
		pattern = pattern.substring(pattern.indexOf(":") + 1).trim();

		if (className.equals(AssetTypes.SITE.getClassName())
			|| className.equals(AssetTypes.LAYOUT.getClassName())) {
		    isCustomAssetSearch = true;
		}
	    }
	}

	int maximumSearchResults = GetterUtil.getInteger(preferences.getValue(
		Utils.CONFIGURATION_MAXIMUM_SEARCH_RESULTS, "5"));

	List<ResultModel> resultModelList;

	if (!isCustomAssetSearch) {
	    // remove asset types which are not for index search
	    // TODO: this should be refacotred
	    assetTypes = ArrayUtil.remove(assetTypes, AssetTypes.SITE.getClassName());
	    assetTypes = ArrayUtil.remove(assetTypes, AssetTypes.LAYOUT.getClassName());

	    try {
		resultModelList = new IndexSearcherImpl(request, pattern, assetTypes, maximumSearchResults, response)
			.getResult();
	    } catch (Exception e) {
		resultModelList = new ArrayList<ResultModel>();
		log.error(e);
	    }
	} else {
	    try {
		resultModelList = new DynamicQueryResultBuilder(assetTypes[0], maximumSearchResults,
			Utils.getThemeDisplay(request), pattern).getResult();
	    } catch (SearchException e) {
		resultModelList = new ArrayList<ResultModel>();
		log.error(e);
	    } catch (Exception e) {
		resultModelList = new ArrayList<ResultModel>();
		log.error(e);
	    }
	}

	ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getWriter(), resultModelList);
	response.getWriter().flush();
    }
}
