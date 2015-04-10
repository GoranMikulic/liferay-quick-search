package mikugo.dev.search;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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

public class ResultModelBuilder {
    private ThemeDisplay themeDisplay;
    private ResourceRequest request;
    private ResourceResponse response;

    private static Log log = LogFactoryUtil.getLog(ResultModelBuilder.class);

    public ResultModelBuilder(ThemeDisplay themeDisplay, ResourceRequest request, ResourceResponse response) {
	this.themeDisplay = themeDisplay;
	this.request = request;
	this.response = response;
    }

    public List<ResultModel> buildList(List<Document> result) throws Exception {
	List<ResultModel> resultModelList = new ArrayList<ResultModel>();

	for (Document document : result) {

	    ResultModel model = buildModel(document);
	    resultModelList.add(model);

	}

	return resultModelList;
    }

    private ResultModel buildModel(Document document) throws Exception {

	String className = document.get(Field.ENTRY_CLASS_NAME);

	String entryTitle = null;
	String entrySummary = null;
	String returnToFullPageURL = (String) request.getAttribute("search.jsp-returnToFullPageURL");
	PortletURL viewFullContentURL = null;
	String viewURL = null;

	AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil
		.getAssetRendererFactoryByClassName(className);

	AssetRenderer assetRenderer = null;

	if (assetRendererFactory != null) {
	    long classPK = GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));

	    long resourcePrimKey = GetterUtil.getLong(document.get(Field.ROOT_ENTRY_CLASS_PK));

	    if (resourcePrimKey > 0) {
		classPK = resourcePrimKey;
	    }

	    AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(className, classPK);

	    assetRenderer = assetRendererFactory.getAssetRenderer(classPK);

	    viewFullContentURL = getViewFullContentURL(request, Utils.getThemeDisplay(request),
		    PortletKeys.ASSET_PUBLISHER, document);

	    viewFullContentURL.setParameter("struts_action", "/asset_publisher/view_content");

	    if (Validator.isNotNull(returnToFullPageURL)) {
		viewFullContentURL.setParameter("returnToFullPageURL", returnToFullPageURL);
	    }

	    viewFullContentURL.setParameter("assetEntryId", String.valueOf(assetEntry.getEntryId()));
	    viewFullContentURL.setParameter("type", assetRendererFactory.getType());

	    // long scopeGroupId =
	    // GetterUtil.getLong(document.get(Field.SCOPE_GROUP_ID));

	    if (Validator.isNotNull(assetRenderer.getUrlTitle())) {
		// if ((assetRenderer.getGroupId() > 0) &&
		// (assetRenderer.getGroupId() != scopeGroupId)) {
		viewFullContentURL.setParameter("groupId", String.valueOf(assetRenderer.getGroupId()));
		// }

		viewFullContentURL.setParameter("urlTitle", assetRenderer.getUrlTitle());
	    }
	    boolean viewInContext = true;
	    if (viewInContext || !assetEntry.isVisible()) {
		// boolean inheritRedirect = true;

		String viewFullContentURLString = viewFullContentURL.toString();

		viewFullContentURLString = HttpUtil.setParameter(viewFullContentURLString, "redirect",
			themeDisplay.getURLCurrent());

		viewURL = assetRenderer.getURLViewInContext((LiferayPortletRequest) request,
			(LiferayPortletResponse) response, viewFullContentURLString);

		log.info(GroupLocalServiceUtil.getGroup(assetEntry.getGroupId()).getFriendlyURL());
	    } else {
		viewURL = viewFullContentURL.toString();
	    }
	} else {
	    String portletId = document.get(Field.PORTLET_ID);

	    viewFullContentURL = getViewFullContentURL(request, Utils.getThemeDisplay(request), portletId, document);

	    if (Validator.isNotNull(returnToFullPageURL)) {
		viewFullContentURL.setParameter("returnToFullPageURL", returnToFullPageURL);
	    }

	    viewURL = viewFullContentURL.toString();
	}

	Indexer indexer = IndexerRegistryUtil.getIndexer(className);

	if (indexer != null) {
	    String snippet = document.get(Field.SNIPPET);

	    Summary summary = indexer.getSummary(document, Utils.getThemeDisplay(request).getLocale(), snippet,
		    viewFullContentURL);

	    entryTitle = summary.getTitle();
	    entrySummary = summary.getContent();
	} else if (assetRenderer != null) {
	    entryTitle = assetRenderer.getTitle(Utils.getThemeDisplay(request).getLocale());
	    entrySummary = assetRenderer.getSearchSummary(Utils.getThemeDisplay(request).getLocale());
	}

	if ((assetRendererFactory == null)) {
	    viewURL = viewFullContentURL.toString();
	}

	viewURL = checkViewURL(themeDisplay, viewURL, themeDisplay.getURLCurrent(), true);

	ResultModel model = new ResultModel();
	model.setTitle(entryTitle);
	model.setSummary(entrySummary);
	model.setDisplayUrl(viewURL);
	model.setAssetType(getUserFriendlyClassName(className));

	return model;
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

    private String getUserFriendlyClassName(String className) {
	if (Utils.MODEL_FILE.equals(className)) {
	    return "file";
	} else if (Utils.MODEL_JOURNAL_ARTICLE.equals(className)) {
	    return "article";
	} else if (Utils.MODEL_USER.equals(className)) {
	    return "user";
	} else {
	    return "";
	}
    }
}