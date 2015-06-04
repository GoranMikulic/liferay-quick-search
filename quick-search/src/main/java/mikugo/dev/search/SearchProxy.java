package mikugo.dev.search;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.combined.CombinedSearchImpl;
import mikugo.dev.search.combined.FacetedSearchImpl;
import mikugo.dev.search.helper.Utils;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.SearchException;

/**
 * This class recognizes the search type by pattern and delegates the search
 * request to the different search implementations
 * 
 * @author Goran Mikulic
 *
 */
public class SearchProxy {

    private static Log log = LogFactoryUtil.getLog(SearchProxy.class);

    private SearchSettings settings;
    private ResourceRequest request;
    private ResourceResponse response;
    private String pattern;

    public SearchProxy(SearchSettings settings, ResourceRequest request, ResourceResponse response, String pattern) {
	super();
	this.settings = settings;
	this.request = request;
	this.response = response;
	this.pattern = pattern;
    }

    /**
     * Recognizing pattern and initializing search
     * 
     * @return Returns list of {@link ResultModel}
     */
    public List<ResultModel> search() {

	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	if (Utils.isFaceted(pattern)) {

	    try {
		resultModelList = new FacetedSearchImpl(request, response, pattern, settings.getConfiguredAssetTypes(),
			settings.getMaximumSearchResults()).getResult();
	    } catch (SearchException e) {
		log.error(e);
	    } catch (Exception e) {
		log.error(e);
	    }

	} else {

	    try {
		resultModelList = new CombinedSearchImpl(request, pattern, Utils.filterIndexTypes(settings
			.getConfiguredAssetTypes()), settings.getMaximumSearchResults(), response).getResult();
	    } catch (SearchException e) {
		log.error(e);
	    } catch (Exception e) {
		log.error(e);
	    }

	}

	return resultModelList;

    }

}
