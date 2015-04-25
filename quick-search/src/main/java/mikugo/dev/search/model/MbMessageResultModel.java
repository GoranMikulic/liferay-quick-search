package mikugo.dev.search.model;

public class MbMessageResultModel extends ResultModel {

    public MbMessageResultModel(IndexSearchResult result) {
	super(result);

	// TODO: replace with language util
	setAssetType("Message-Board-Message");
    }

}
