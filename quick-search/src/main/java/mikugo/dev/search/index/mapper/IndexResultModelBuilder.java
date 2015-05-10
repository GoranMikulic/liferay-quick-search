package mikugo.dev.search.index.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.model.IndexResultModelFactory;
import mikugo.dev.search.model.IndexResultResourcesModel;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * This class is responsible for mapping of lucene search results
 * {@link Document} to {@link ResultModel} used for representation in GUI
 * 
 * @author mikugo
 *
 */
public class IndexResultModelBuilder {

    private ThemeDisplay themeDisplay;
    private ResourceRequest request;
    private ResourceResponse response;

    public IndexResultModelBuilder(ThemeDisplay themeDisplay, ResourceRequest request, ResourceResponse response) {
	this.themeDisplay = themeDisplay;
	this.request = request;
	this.response = response;
    }

    /**
     * Mapping a list of {@link Document} to a list of {@link ResultModel}
     * 
     * @param result
     * @return
     * @throws Exception
     */
    public List<ResultModel> buildList(List<Document> result) throws Exception {
	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	for (Document document : result) {

	    ResultModel model = buildModel(document);
	    resultModelList.add(model);

	}

	return resultModelList;
    }

    /**
     * Mapping document result to {@link ResultModel}
     * 
     * @param document
     * @return
     * @throws Exception
     */
    private ResultModel buildModel(Document document) throws Exception {
	return new IndexResultModelFactory(new IndexResultResourcesModel(document, request, response, themeDisplay))
		.getModel(document.get(Field.ENTRY_CLASS_NAME));
    }

}
