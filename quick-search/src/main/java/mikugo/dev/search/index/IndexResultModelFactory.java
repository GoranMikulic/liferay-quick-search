package mikugo.dev.search.index;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.index.mapper.IndexResultMapperImpl;
import mikugo.dev.search.index.mapper.MbMessageResultMapper;
import mikugo.dev.search.index.mapper.UserResultMapper;
import mikugo.dev.search.model.ResultModel;

/**
 * Factory class for {@link ResultModel} Types
 * 
 * @author Goran Mikulic
 *
 */
public class IndexResultModelFactory {

    private IndexResultResourcesModel indexResultResourcesModel;

    public IndexResultModelFactory(IndexResultResourcesModel indexResultResourcesModel) {
	this.indexResultResourcesModel = indexResultResourcesModel;
    }

    /**
     * Delegates Mapping to specific model class
     * 
     * @param entryType
     * @param liferayResult
     * @return
     * @throws Exception
     */
    public ResultModel getModel(String entryType) throws Exception {

	if (AssetTypes.USER.getClassName().equals(entryType)) {

	    return new UserResultMapper(indexResultResourcesModel).getResultModel();

	} else if (AssetTypes.MBM_MESSAGE.getClassName().equals(entryType)) {

	    return new MbMessageResultMapper(indexResultResourcesModel).getResultModel();

	} else {

	    return new IndexResultMapperImpl(indexResultResourcesModel).getResultModel();

	}
    }

}
