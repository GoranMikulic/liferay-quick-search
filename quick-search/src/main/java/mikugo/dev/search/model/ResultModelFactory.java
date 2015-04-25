package mikugo.dev.search.model;

import mikugo.dev.search.helper.AssetTypes;

/**
 * Factory class for {@link ResultModel} Types
 * 
 * @author mikugo
 *
 */
public class ResultModelFactory {
    
    /**
     * Delegates Mapping to specific model class
     * 
     * @param entryType
     * @param result
     * @return
     */
    public ResultModel getModel(String entryType, IndexSearchResult result) {

	if (AssetTypes.USER.getClassName().equals(entryType)) {
	    return new UserResultModel(result);
	} else if (AssetTypes.MBM_MESSAGE.getClassName().equals(entryType)) {
	    return new MbMessageResultModel(result);
	} else {
	    return new ResultModel(result);
	    
	}

    }
}
