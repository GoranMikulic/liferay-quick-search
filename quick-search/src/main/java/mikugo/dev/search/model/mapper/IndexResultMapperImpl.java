package mikugo.dev.search.model.mapper;

import mikugo.dev.search.model.ResultModel;

public class IndexResultMapperImpl implements IndexResultMapper {

    protected LiferayIndexSearchResultProcessor processor;

    public IndexResultMapperImpl(LiferayIndexSearchResultProcessor processor) {
	this.processor = processor;
    }

    @Override
    public ResultModel getResultModel() {
	return new ResultModel(processor.getEntryTitle(), processor.getEntrySummary(), processor.getViewURL(),
		processor.getUserFriendlyClassName(), processor.getGroupName());
    }
}
