package mikugo.dev.search.model;

/**
 * Model class for search results
 * 
 * @author Goran Mikulic
 *
 */
public class ResultModel {

    private String title;
    private String summary;
    private String displayUrl;
    private String assetType;
    private String metadata;

    public ResultModel(String title, String summary, String displayUrl, String assetType, String metadata) {
	this.title = title;
	this.summary = summary;
	this.displayUrl = displayUrl;
	this.assetType = assetType;
	this.metadata = metadata;
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
	this.summary = summary.length() > 200 ? summary.substring(0, 200) + "..." : summary;
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

    public String getMetadata() {
	return metadata;
    }

    public void setMetadata(String metadata) {
	this.metadata = metadata;
    }

}
