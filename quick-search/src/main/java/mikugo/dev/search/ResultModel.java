package mikugo.dev.search;

public class ResultModel {

	private String title;
	private String summary;
	private String displayUrl;
	private String assetType;
	
	public ResultModel(String title, String summary, String displayUrl, String assetType) {
	    this.title = title;
	    this.summary = summary;
	    this.displayUrl = displayUrl;
	    this.assetType = assetType;
	}
	
	public String getDisplayUrl() {
		return displayUrl;
	}

	public void setDisplayUrl(String displayUrl) {
		this.displayUrl = displayUrl;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

}
