package mikugo.dev.search;

public class SearchSettings {

    private int maximumSearchResults;
    private String[] configuredAssetTypes;

    /**
     * @return the maximumSearchResults
     */
    public int getMaximumSearchResults() {
	return maximumSearchResults;
    }

    /**
     * @param maximumSearchResults
     *            the maximumSearchResults to set
     */
    public void setMaximumSearchResults(int maximumSearchResults) {
	this.maximumSearchResults = maximumSearchResults;
    }

    /**
     * @return the configuredAssetTypes
     */
    public String[] getConfiguredAssetTypes() {
	return configuredAssetTypes;
    }

    /**
     * @param configuredAssetTypes
     *            the configuredAssetTypes to set
     */
    public void setConfiguredAssetTypes(String[] configuredAssetTypes) {
	this.configuredAssetTypes = configuredAssetTypes;
    }

}
