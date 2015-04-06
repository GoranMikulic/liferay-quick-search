package mikugo.dev.search;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.FacetedSearcher;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.wiki.model.WikiPage;
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
			}

		}
	}

	public void serveResults(ResourceRequest request,
			ResourceResponse response, String pattern) throws SearchException {

		List<Document> result = search(request, pattern);
		
		ObjectMapper mapper = new ObjectMapper();
		

	}

	private List<Document> search(ResourceRequest request, String pattern)
			throws SearchException {
		Indexer indexer = FacetedSearcher.getInstance();
		SearchContext searchContext = new SearchContext();

		String[] entryClassNames = { JournalArticle.class.toString(),
				User.class.toString(), DLFileEntry.class.toString(),
				WikiPage.class.toString() };
		searchContext.setEntryClassNames(entryClassNames);
		searchContext.setKeywords(pattern);
		searchContext.setCompanyId(getThemeDisplay(request).getCompanyId());
		searchContext.setLike(true);

		Hits hits = indexer.search(searchContext);

		List<Document> documents = hits.toList();

		for (Document document : documents) {
			log.info("Doc found" + document.toString());
		}
		log.info(hits.getQuery().toString());

		return documents;

	}

	public static ThemeDisplay getThemeDisplay(PortletRequest request) {
		if (null == request) {
			throw new IllegalArgumentException("request is null");
		}

		return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
	}
}
