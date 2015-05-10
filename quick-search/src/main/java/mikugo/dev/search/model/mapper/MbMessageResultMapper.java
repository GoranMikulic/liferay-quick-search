package mikugo.dev.search.model.mapper;

import mikugo.dev.search.model.ResultModel;

/**
 * Model class for message board results
 * 
 * @author mikugo
 *
 */
public class MbMessageResultMapper extends IndexResultMapperImpl {

    public MbMessageResultMapper(LiferayIndexSearchResultProcessor resultMapper) {
	super(resultMapper);
    }

    @Override
    public ResultModel getResultModel() {
	ResultModel resultModel = super.getResultModel();
	resultModel.setAssetType("Message-Board-Message");
	return resultModel;
    }

}
