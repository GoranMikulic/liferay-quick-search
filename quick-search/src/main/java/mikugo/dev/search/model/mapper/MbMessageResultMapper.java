package mikugo.dev.search.model.mapper;

import mikugo.dev.search.model.ResultModel;

/**
 * Model class for message board results
 * @author mikugo
 *
 */
public class MbMessageResultMapper extends ResultModel {

    public MbMessageResultMapper(LiferayIndexSearchResultProcessor resultMapper) {
	super(resultMapper);

	// TODO: replace with language util
	setAssetType("Message-Board-Message");
    }

}
