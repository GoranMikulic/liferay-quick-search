package mikugo.dev.search.helper;

import java.util.List;

import javax.portlet.ResourceRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.FacetedSearcher;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.facet.AssetEntriesFacet;
import com.liferay.portal.kernel.search.facet.Facet;

public class IndexSearcher {
	
	private static Log log = LogFactoryUtil.getLog(IndexSearcher.class);
	
	public List<Document> search(ResourceRequest request, String pattern, String[] entryClassNames, int maximumSearchResults)
			throws SearchException {
		
		Indexer indexer = FacetedSearcher.getInstance();
		SearchContext searchContext = new SearchContext();
		
		searchContext.setEntryClassNames(entryClassNames);
		Facet assetEntriesFacet = new AssetEntriesFacet(searchContext);

		assetEntriesFacet.setStatic(true);

		searchContext.addFacet(assetEntriesFacet);
		searchContext.setKeywords(pattern);
		searchContext.setCompanyId(Utils.getThemeDisplay(request).getCompanyId());
		searchContext.setLike(true);
		searchContext.setStart(0);
		searchContext.setEnd(maximumSearchResults);
		
		Hits hits = indexer.search(searchContext);
		
		List<Document> documents = hits.toList();

		log.info("Documents found: " + documents.size());
		log.info(hits.getQuery().toString());

		return documents;

	}
}
