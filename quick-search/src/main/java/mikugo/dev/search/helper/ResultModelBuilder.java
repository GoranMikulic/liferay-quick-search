package mikugo.dev.search.helper;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import mikugo.dev.search.model.Result;
import mikugo.dev.search.model.ResultModel;
import mikugo.dev.search.model.ResultModelFactory;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.theme.ThemeDisplay;

public class ResultModelBuilder {

    private ThemeDisplay themeDisplay;
    private ResourceRequest request;
    private ResourceResponse response;

    // private static Log log = LogFactoryUtil.getLog(ResultModelBuilder.class);

    public ResultModelBuilder(ThemeDisplay themeDisplay, ResourceRequest request, ResourceResponse response) {
	this.themeDisplay = themeDisplay;
	this.request = request;
	this.response = response;
    }

    public List<ResultModel> buildList(List<Document> result) throws Exception {
	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	for (Document document : result) {

	    ResultModel model = buildModel(document);
	    resultModelList.add(model);

	}

	return resultModelList;
    }

    private ResultModel buildModel(Document document) throws Exception {
	return new ResultModelFactory().getModel(document.get(Field.ENTRY_CLASS_NAME), new Result(document, request,
		response, themeDisplay));
    }

}
