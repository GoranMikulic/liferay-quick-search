package mikugo.dev.search.model;

import mikugo.dev.search.helper.AssetTypes;

/**
 * Factory class for {@link ResultModel} Types
 * 
 * @author mikugo
 *
 */
public class ResultModelFactory {
    public ResultModel getModel(String entryType, Result result) {

	if (AssetTypes.USER.getClassName().equals(entryType)) {
	    return new UserResultModel(result);
	} else {
	    return new ResultModel(result);
	    
	}

    }
}
