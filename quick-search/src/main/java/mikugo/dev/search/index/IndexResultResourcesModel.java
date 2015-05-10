package mikugo.dev.search.index;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.theme.ThemeDisplay;

public class IndexResultResourcesModel {

    private Document document;
    private ResourceRequest request;
    private ResourceResponse response;
    private ThemeDisplay themeDisplay;

    public IndexResultResourcesModel(Document document, ResourceRequest request, ResourceResponse response,
	    ThemeDisplay themeDisplay) {

	this.document = document;
	this.request = request;
	this.response = response;
	this.themeDisplay = themeDisplay;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
	return document;
    }

    /**
     * @param document
     *            the document to set
     */
    public void setDocument(Document document) {
	this.document = document;
    }

    /**
     * @return the request
     */
    public ResourceRequest getRequest() {
	return request;
    }

    /**
     * @param request
     *            the request to set
     */
    public void setRequest(ResourceRequest request) {
	this.request = request;
    }

    /**
     * @return the response
     */
    public ResourceResponse getResponse() {
	return response;
    }

    /**
     * @param response
     *            the response to set
     */
    public void setResponse(ResourceResponse response) {
	this.response = response;
    }

    /**
     * @return the themeDisplay
     */
    public ThemeDisplay getThemeDisplay() {
	return themeDisplay;
    }

    /**
     * @param themeDisplay
     *            the themeDisplay to set
     */
    public void setThemeDisplay(ThemeDisplay themeDisplay) {
	this.themeDisplay = themeDisplay;
    }

}
