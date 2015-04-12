package mikugo.dev.search.model;

import com.liferay.portlet.asset.model.AssetEntry;

/**
 * This class represents a result model for {@link AssetEntry}
 * 
 * @author mikugo
 *
 */
public class ResultModel {

    private String title;
    private String summary;
    private String displayUrl;
    private String assetType;
    private String metadata;

    public ResultModel(Result result) {

	setTitle(result.getEntryTitle());
	setSummary(result.getEntrySummary());
	setDisplayUrl(result.getViewURL());
	setAssetType(result.getUserFriendlyClassName());
	writeMetadata(result);
    }

    public void writeMetadata(Result result) {
	setMetadata(result.getGroupName());
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
