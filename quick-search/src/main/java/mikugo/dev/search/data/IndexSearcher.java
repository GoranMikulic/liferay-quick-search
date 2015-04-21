package mikugo.dev.search.data;

import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.model.ResultModel;

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

/**
 * This class is responsible to build process the search and serve the results.
 * Connection between Portlet and Liferay Search API
 * 
 * @author mikugo
 *
 */
public class IndexSearcher implements Search {

    private static Log log = LogFactoryUtil.getLog(IndexSearcher.class);

    private ResourceRequest request;
    private String pattern;
    private String[] entryClassNames;
    private int maximumSearchResults;
    private ResourceResponse response;

    public IndexSearcher(ResourceRequest request, String pattern, String[] entryClassNames, int maximumSearchResults,
	    ResourceResponse response) {
	this.request = request;
	this.pattern = pattern;
	this.entryClassNames = entryClassNames;
	this.maximumSearchResults = maximumSearchResults;
    }

    public List<Document> search(ResourceRequest request, String pattern, String[] entryClassNames,
	    int maximumSearchResults) throws SearchException {

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

	log.debug("Documents found: " + documents.size());
	log.debug(hits.getQuery().toString());

	return documents;

    }

    @Override
    public List<ResultModel> getResult() throws Exception {
	List<Document> result = search(request, pattern, entryClassNames, maximumSearchResults);
	return new ResultModelBuilder(Utils.getThemeDisplay(request), request, response).buildList(result);
    }
}
