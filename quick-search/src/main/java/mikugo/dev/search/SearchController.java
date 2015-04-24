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

    private boolean isFaceted(String pattern) {
	// Regex should search for following pattern <type>:<keyword>
	Pattern regex = Pattern.compile("([A-Z,a-z])+\\w:(\\s)*([A-Z,a-z])*");
	Matcher matcher = regex.matcher(pattern);
	return matcher.matches();
    }

    public void serveResults(ResourceRequest request, ResourceResponse response, String pattern)
	    throws JsonGenerationException, JsonMappingException, IOException {

	PortletPreferences preferences = request.getPreferences();

	String[] assetTypes = preferences.getValues(Utils.CONFIGURATION_ASSET_TYPES, AssetTypes.getAllClassNames());

	int maximumSearchResults = GetterUtil.getInteger(preferences.getValue(
		Utils.CONFIGURATION_MAXIMUM_SEARCH_RESULTS, "5"));

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	if (isFaceted(pattern)) {
	    String searchType = pattern.substring(0, pattern.indexOf(":"));
	    pattern = pattern.substring(pattern.indexOf(":")+1).trim();
	    
	    log.info(searchType);

	    if (ArrayUtil.contains(AssetTypes.getDynamicQueryReadableNames(), searchType)) {
		log.info("starting faceted dynamic query search");
		log.info("for class: " + AssetTypes.getClassName(searchType));
		log.info("for pattern: " + pattern);
		
		try {
		    resultModelList = new DynamicQueryResultBuilder(AssetTypes.getClassName(searchType),
			    maximumSearchResults, Utils.getThemeDisplay(request), pattern).getResult();
		} catch (SearchException e) {
		    log.error(e);
		} catch (Exception e) {
		    log.error(e);
		}
	    } else if (ArrayUtil.contains(AssetTypes.getIndexSearchReadableNames(), searchType)) {
		log.info("starting faceted index query search");
		log.info("for class: " + AssetTypes.getClassName(searchType));
		log.info("for pattern: " + pattern);
		
		try {
		    resultModelList = new IndexSearcherImpl(request, pattern,
			    new String[] { AssetTypes.getClassName(searchType) }, maximumSearchResults, response)
			    .getResult();
		} catch (Exception e) {
		    log.error(e);
		}
	    }
	} else {

	    // remove asset types which are not for index search
	    // TODO: this should be refacotred
	    assetTypes = ArrayUtil.remove(assetTypes, AssetTypes.SITE.getClassName());
	    assetTypes = ArrayUtil.remove(assetTypes, AssetTypes.LAYOUT.getClassName());

	    try {
		resultModelList = new IndexSearcherImpl(request, pattern, assetTypes, maximumSearchResults, response)
			.getResult();
	    } catch (Exception e) {
		log.error(e);
	    }

	}

	ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getWriter(), resultModelList);
	response.getWriter().flush();
    }
}
