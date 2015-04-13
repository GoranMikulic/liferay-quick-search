package mikugo.dev.search.model;

import mikugo.dev.search.helper.AssetTypes;

public class ResultModelFactory {
    public ResultModel getModel(String entryType, Result result) {

	if (AssetTypes.USER.getClassName().equals(entryType)) {
	    return new UserResultModel(result);
	} else {
	    return new ResultModel(result);
	    
	}

    }
}
