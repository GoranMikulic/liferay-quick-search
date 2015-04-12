package mikugo.dev.search.model;

import mikugo.dev.search.helper.Utils;

public class ResultModelFactory {
    public ResultModel getModel(String entryType, Result result) {

	if (Utils.MODEL_USER.equals(entryType)) {
	    return new UserResultModel(result);
	    
	} else {
	    return new ResultModel(result);
	    
	}

    }
}
