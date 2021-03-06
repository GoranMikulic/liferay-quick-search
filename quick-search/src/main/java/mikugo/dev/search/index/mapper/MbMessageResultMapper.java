package mikugo.dev.search.index.mapper;

import mikugo.dev.search.index.IndexResultResourcesModel;
import mikugo.dev.search.model.ResultModel;

/**
 * Model class for message board results
 * 
 * @author Goran Mikulic
 *
 */
public class MbMessageResultMapper extends IndexResultMapperImpl {

    public MbMessageResultMapper(IndexResultResourcesModel indexResultResourcesModel) throws Exception {
	super(indexResultResourcesModel);
    }

    @Override
    public ResultModel getResultModel() {
	ResultModel resultModel = super.getResultModel();
	resultModel.setAssetType("Message-Board-Message");
	return resultModel;
    }

}
