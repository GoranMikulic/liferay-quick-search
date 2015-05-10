package mikugo.dev.search.model;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.model.mapper.LiferayIndexSearchResultProcessor;
import mikugo.dev.search.model.mapper.MbMessageResultMapper;
import mikugo.dev.search.model.mapper.UserResultMapper;

/**
 * Factory class for {@link ResultModel} Types
 * 
 * @author mikugo
 *
 */
public class IndexResultModelFactory {

    /**
     * Delegates Mapping to specific model class
     * 
     * @param entryType
     * @param liferayResult
     * @return
     */
    public ResultModel getModel(String entryType, LiferayIndexSearchResultProcessor liferayResult) {

	if (AssetTypes.USER.getClassName().equals(entryType)) {
	    return new UserResultMapper(liferayResult);
	} else if (AssetTypes.MBM_MESSAGE.getClassName().equals(entryType)) {
	    return new MbMessageResultMapper(liferayResult);
	} else {
	    return new ResultModel(liferayResult);
	}
    }
}
