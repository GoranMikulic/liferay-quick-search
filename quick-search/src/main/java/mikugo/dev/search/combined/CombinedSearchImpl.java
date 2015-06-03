package mikugo.dev.search.combined;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.data.Search;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.index.IndexSearcherImpl;
import mikugo.dev.search.model.ResultModel;
import mikugo.dev.search.query.GroupSearcherImpl;
import mikugo.dev.search.query.LayoutSearchImpl;

import com.liferay.portal.kernel.search.SearchException;

/**
 * This class bundles all search implementations
 * 
 * @author mikugo
 *
 */
public class CombinedSearchImpl implements Search {

    private LayoutSearchImpl layoutSearchImpl;
    private GroupSearcherImpl groupSearchImpl;
    private IndexSearcherImpl indexSearcherImpl;

    public CombinedSearchImpl(ResourceRequest request, String pattern, String[] entryClassNames,
	    int maximumSearchResults, ResourceResponse response) {

	this.layoutSearchImpl = new LayoutSearchImpl(pattern, maximumSearchResults, Utils.getThemeDisplay(request));
	this.groupSearchImpl = new GroupSearcherImpl(pattern, maximumSearchResults, Utils.getThemeDisplay(request));
	this.indexSearcherImpl = new IndexSearcherImpl(request, pattern, entryClassNames, maximumSearchResults,
		response);

    }

    @Override
    public List<ResultModel> getResult() throws SearchException, Exception {

	List<ResultModel> result = new ArrayList<ResultModel>();
	result.addAll(indexSearcherImpl.getResult());
	result.addAll(groupSearchImpl.getResult());
	result.addAll(layoutSearchImpl.getResult());

	return result;
    }

}
