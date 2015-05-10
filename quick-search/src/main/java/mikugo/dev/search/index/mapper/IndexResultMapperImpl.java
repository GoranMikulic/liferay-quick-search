package mikugo.dev.search.index.mapper;

import mikugo.dev.search.helper.LiferayIndexSearchResultProcessor;
import mikugo.dev.search.model.IndexResultResourcesModel;
import mikugo.dev.search.model.ResultModel;

public class IndexResultMapperImpl implements IndexResultMapper {

    protected LiferayIndexSearchResultProcessor processor;

    public IndexResultMapperImpl(IndexResultResourcesModel indexResultResourcesModel) throws Exception {

	this.processor = new LiferayIndexSearchResultProcessor(indexResultResourcesModel.getDocument(),
		indexResultResourcesModel.getRequest(), indexResultResourcesModel.getResponse(),
		indexResultResourcesModel.getThemeDisplay());
    }

    @Override
    public ResultModel getResultModel() {
	return new ResultModel(processor.getEntryTitle(), processor.getEntrySummary(), processor.getViewURL(),
		processor.getUserFriendlyClassName(), processor.getGroupName());
    }
}
