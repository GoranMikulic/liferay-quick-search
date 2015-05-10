package mikugo.dev.search.model.mapper;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

import mikugo.dev.search.helper.AssetTypes;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;

/**
 * Respresenting results from liferay search api
 * 
 * @author mikugo
 *
 */
public class LiferayIndexSearchResultProcessor {

    private String className;
    private String entryTitle;
    private String entrySummary;
    private String returnToFullPageURL;
    private PortletURL viewFullContentURL;
    private String viewURL;
    private String groupName;
    private AssetRenderer assetRenderer;
    private AssetEntry assetEntry;
    private AssetRendererFactory assetRendererFactory;
    private Indexer indexer;
    private Summary summary;
    private ThemeDisplay themeDisplay;

    public LiferayIndexSearchResultProcessor(Document document, ResourceRequest request, ResourceResponse response,
	    ThemeDisplay themeDisplay) throws Exception {
	this.className = document.get(Field.ENTRY_CLASS_NAME);
	this.returnToFullPageURL = (String) request.getAttribute("search.jsp-returnToFullPageURL");
	this.themeDisplay = themeDisplay;
	this.assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);
	processResult(document, request, response, themeDisplay);
    }

    public void processResult(Document document, ResourceRequest request, ResourceResponse response,
	    ThemeDisplay themeDisplay) throws Exception {
	if (assetRendererFactory != null) {
	    long classPK = GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));

	    long resourcePrimKey = GetterUtil.getLong(document.get(Field.ROOT_ENTRY_CLASS_PK));

	    if (resourcePrimKey > 0) {
		classPK = resourcePrimKey;
	    }

	    assetEntry = AssetEntryLocalServiceUtil.getEntry(className, classPK);

	    assetRenderer = assetRendererFactory.getAssetRenderer(classPK);

	    viewFullContentURL = getViewFullContentURL(request, themeDisplay, PortletKeys.ASSET_PUBLISHER, document);

	    viewFullContentURL.setParameter("struts_action", "/asset_publisher/view_content");

	    if (Validator.isNotNull(returnToFullPageURL)) {
		viewFullContentURL.setParameter("returnToFullPageURL", returnToFullPageURL);
	    }

	    viewFullContentURL.setParameter("assetEntryId", String.valueOf(assetEntry.getEntryId()));
	    viewFullContentURL.setParameter("type", assetRendererFactory.getType());

	    if (Validator.isNotNull(assetRenderer.getUrlTitle())) {
		viewFullContentURL.setParameter("groupId", String.valueOf(assetRenderer.getGroupId()));
		viewFullContentURL.setParameter("urlTitle", assetRenderer.getUrlTitle());
	    }
	    boolean viewInContext = true;
	    if (viewInContext || !assetEntry.isVisible()) {

		String viewFullContentURLString = viewFullContentURL.toString();
		viewFullContentURLString = HttpUtil.setParameter(viewFullContentURLString, "redirect",
			themeDisplay.getURLCurrent());

		viewURL = assetRenderer.getURLViewInContext((LiferayPortletRequest) request,
			(LiferayPortletResponse) response, viewFullContentURLString);

		groupName = GroupLocalServiceUtil.getGroup(assetEntry.getGroupId()).getDescriptiveName(
			themeDisplay.getLocale());

	    } else {
		viewURL = viewFullContentURL.toString();
	    }
	} else {
	    String portletId = document.get(Field.PORTLET_ID);

	    viewFullContentURL = getViewFullContentURL(request, themeDisplay, portletId, document);

	    if (Validator.isNotNull(returnToFullPageURL)) {
		viewFullContentURL.setParameter("returnToFullPageURL", returnToFullPageURL);
	    }

	    viewURL = viewFullContentURL.toString();
	}

	Indexer indexer = IndexerRegistryUtil.getIndexer(className);

	if (indexer != null) {
	    String snippet = document.get(Field.SNIPPET);

	    Summary summary = indexer.getSummary(document, themeDisplay.getLocale(), snippet, viewFullContentURL);
	    if (summary != null) {
		entryTitle = summary.getTitle();
		entrySummary = summary.getContent();
	    }
	} else if (assetRenderer != null) {
	    entryTitle = assetRenderer.getTitle(themeDisplay.getLocale());
	    entrySummary = assetRenderer.getSearchSummary(themeDisplay.getLocale());
	}

	if ((assetRendererFactory == null)) {
	    viewURL = viewFullContentURL.toString();
	}

	viewURL = checkViewURL(themeDisplay, viewURL, themeDisplay.getURLCurrent(), true);
    }

    private PortletURL getViewFullContentURL(ResourceRequest request, ThemeDisplay themeDisplay, String portletId,
	    Document document) throws Exception {
	long groupId = GetterUtil.getLong(document.get(Field.GROUP_ID));

	if (groupId == 0) {
	    Layout layout = themeDisplay.getLayout();

	    groupId = layout.getGroupId();
	}

	long scopeGroupId = GetterUtil.getLong(document.get(Field.SCOPE_GROUP_ID));

	if (scopeGroupId == 0) {
	    scopeGroupId = themeDisplay.getScopeGroupId();
	}

	long plid = LayoutConstants.DEFAULT_PLID;

	Layout layout = (Layout) request.getAttribute(WebKeys.LAYOUT);

	if (layout != null) {
	    plid = layout.getPlid();
	}

	if (plid == 0) {
	    plid = LayoutServiceUtil.getDefaultPlid(groupId, scopeGroupId, portletId);
	}

	PortletURL portletURL = PortletURLFactoryUtil.create(request, portletId, plid, PortletRequest.RENDER_PHASE);

	portletURL.setPortletMode(PortletMode.VIEW);
	portletURL.setWindowState(WindowState.MAXIMIZED);

	return portletURL;
    }

    private String checkViewURL(ThemeDisplay themeDisplay, String viewURL, String currentURL, boolean inheritRedirect) {
	if (Validator.isNotNull(viewURL) && viewURL.startsWith(themeDisplay.getURLPortal())) {
	    viewURL = HttpUtil.setParameter(viewURL, "inheritRedirect", inheritRedirect);

	    if (!inheritRedirect) {
		viewURL = HttpUtil.setParameter(viewURL, "redirect", currentURL);
	    }
	}

	return viewURL;
    }

    /**
     * Returns localized asset name
     * 
     * @return localized asset name
     */
    public String getUserFriendlyClassName() {
	return LanguageUtil.get(themeDisplay.getLocale(), AssetTypes.getReadableName(this.className));
    }

    public String getEntryTitle() {
	return entryTitle;
    }

    public String getEntrySummary() {
	return entrySummary;
    }

    public String getGroupName() {
	return groupName;
    }

    public Indexer getIndexer() {
	return indexer;
    }

    public Summary getSummary() {
	return summary;
    }

    public String getViewURL() {
	return viewURL;
    }

    public String getClassName() {
	return className;
    }

    public String getReturnToFullPageURL() {
	return returnToFullPageURL;
    }

    public PortletURL getViewFullContentURL() {
	return viewFullContentURL;
    }

    public AssetRenderer getAssetRenderer() {
	return assetRenderer;
    }

    public AssetEntry getAssetEntry() {
	return assetEntry;
    }

    public AssetRendererFactory getAssetRendererFactory() {
	return assetRendererFactory;
    }

    public ThemeDisplay getThemeDisplay() {
	return themeDisplay;
    }
}
