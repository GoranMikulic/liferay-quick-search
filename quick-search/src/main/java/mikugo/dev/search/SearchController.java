package mikugo.dev.search;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class SearchController extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(SearchController.class);

	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		super.serveResource(resourceRequest, resourceResponse);

		if (resourceRequest.getParameter("action").equals("search")) {

			try {
				serveResults(resourceRequest, resourceResponse,
						resourceRequest.getParameter("pattern"));
			} catch (SearchException e) {
				log.error(e);
			} catch (Exception e) {
				log.error(e);
			}

		}
	}

	public void serveResults(ResourceRequest request,
			ResourceResponse response, String pattern) throws Exception {

		IndexSearcher searcher = new IndexSearcher();
		List<Document> result = searcher.search(request, pattern);
		List<ResultModel> resultModelList = Utils.convertResult(result, request);

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), resultModelList);
		response.getWriter().flush();
	}
}
