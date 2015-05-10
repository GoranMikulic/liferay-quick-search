package mikugo.dev.search.impl;

import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.data.Search;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.model.ResultModel;
import mikugo.dev.search.model.mapper.IndexResultModelBuilder;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.FacetedSearcher;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.AssetEntriesFacet;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.model.Group;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Search Implementation for a Liferay index search
 * 
 * @author mikugo
 *
 */
public class IndexSearcherImpl implements Search {

    private static Log log = LogFactoryUtil.getLog(IndexSearcherImpl.class);

    private ResourceRequest request;
    private String pattern;
    private String[] entryClassNames;
    private int maximumSearchResults;
    private ResourceResponse response;

    /**
     * Default constructor
     * 
     * @param request
     * @param pattern
     * @param entryClassNames
     *            - ClassNames of asset types to search
     * @param maximumSearchResults
     *            - Amount of maximum search result
     * @param response
     */
    public IndexSearcherImpl(ResourceRequest request, String pattern, String[] entryClassNames,
	    int maximumSearchResults, ResourceResponse response) {
	this.request = request;
	this.pattern = pattern;
	this.entryClassNames = entryClassNames;
	this.maximumSearchResults = maximumSearchResults;
    }

    /**
     * Processing search and returns a List of {@link Document}
     * 
     * @param request
     * @param pattern
     * @param entryClassNames
     * @param maximumSearchResults
     * @return A List of {@link Document}
     * @throws SystemException
     * @throws PortalException
     */
    public List<Document> search(ResourceRequest request, String pattern, String[] entryClassNames,
	    int maximumSearchResults) throws SystemException, PortalException {

	// TODO: set fuzzy search without string manipuliation
	pattern = pattern + "*";

	Indexer indexer = FacetedSearcher.getInstance();
	SearchContext searchContext = new SearchContext();

	searchContext.setEntryClassNames(entryClassNames);
	Facet assetEntriesFacet = new AssetEntriesFacet(searchContext);

	assetEntriesFacet.setStatic(true);

	searchContext.setGroupIds(getGroupIds(Utils.getThemeDisplay(request)));
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

    /**
     * Returns the groupIDs of the themeDisplays users groups (sites)
     * 
     * @param themeDisplay
     * @return The groupIDs of the themeDisplays users groups (sites)
     * @throws SystemException
     * @throws PortalException
     */
    private long[] getGroupIds(ThemeDisplay themeDisplay) throws SystemException, PortalException {

	List<Group> groups = themeDisplay.getUser().getMySiteGroups();
	long[] groupIds = new long[groups.size()];

	for (int i = 0; i < groups.size(); i++) {
	    groupIds[i] = groups.get(i).getGroupId();
	}

	return groupIds;
    }

    /**
     * Returns a list of {@link ResultModel}
     */
    @Override
    public List<ResultModel> getResult() throws Exception {
	List<Document> result = search(request, pattern, entryClassNames, maximumSearchResults);
	return new IndexResultModelBuilder(Utils.getThemeDisplay(request), request, response).buildList(result);
    }
}
