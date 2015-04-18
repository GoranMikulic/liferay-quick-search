package mikugo.dev.search;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.helper.DynamicQuerySearcher;
import mikugo.dev.search.helper.IndexSearcher;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.model.ResultModel;

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

	    try {
		serveResults(resourceRequest, resourceResponse, resourceRequest.getParameter("pattern"));
	    } catch (SearchException e) {
		log.error(e);
	    } catch (Exception e) {
		log.error(e);
	    }

	}
    }

    public void serveResults(ResourceRequest request, ResourceResponse response, String pattern) throws Exception {

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
	    //remove asset types which are not for index search
	    //TODO: this should be refacotred
	    assetTypes = ArrayUtil.remove(assetTypes, AssetTypes.SITE.getClassName());
	    assetTypes = ArrayUtil.remove(assetTypes, AssetTypes.LAYOUT.getClassName());
	    resultModelList = new IndexSearcher(request, pattern, assetTypes, maximumSearchResults, response)
		    .getResult();
	} else {
	    resultModelList = new DynamicQuerySearcher(assetTypes[0], pattern, maximumSearchResults,
		    Utils.getThemeDisplay(request)).getResult();
	}

	ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getWriter(), resultModelList);
	response.getWriter().flush();
    }
}
