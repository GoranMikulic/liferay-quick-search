package mikugo.dev.search.helper;

import java.util.ArrayList;
import java.util.List;

import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

public class DynamicQueryResultBuilder {

    private String className;
    private DynamicQuery query;
    private int maxSearchResults;
    private ThemeDisplay themeDisplay;

    public DynamicQueryResultBuilder(String className, DynamicQuery query, int maxSearchResults,
	    ThemeDisplay themeDisplay) {
	this.className = className;
	this.query = query;
	this.maxSearchResults = maxSearchResults;
	this.themeDisplay = themeDisplay;
    }

    @SuppressWarnings("unchecked")
    public List<ResultModel> getResult() throws SystemException, PortalException {
	if (className.equals(AssetTypes.SITE.getClassName())) {
	    return getGroupResult(GroupLocalServiceUtil.dynamicQuery(this.query, 0, this.maxSearchResults));
	} else if (className.equals(AssetTypes.LAYOUT.getClassName())) {
	    return getLayoutResult(LayoutLocalServiceUtil.dynamicQuery(query, 0, this.maxSearchResults));
	}
	return null;
    }

    private List<ResultModel> getLayoutResult(List<Layout> result) throws PortalException, SystemException {
	List<ResultModel> resultModel = new ArrayList<ResultModel>();

	for (Layout layout : result) {
	    resultModel.add(new ResultModel(layout.getName(), layout.getDescription(), layout.getFriendlyURL(),
		    LanguageUtil.get(themeDisplay.getLocale(), AssetTypes.LAYOUT.getReadableName()), layout.getGroup()
			    .getDescriptiveName()));
	}

	return resultModel;
    }

    private List<ResultModel> getGroupResult(List<Group> result) throws PortalException, SystemException {
	List<ResultModel> resultModel = new ArrayList<ResultModel>();

	for (Group group : result) {
	    resultModel.add(new ResultModel(group.getDescriptiveName(), group.getDescription(), "/web" + group
		    .getFriendlyURL(), LanguageUtil.get(themeDisplay.getLocale(),
		    AssetTypes.SITE.getReadableName()), ""));
	}

	return resultModel;
    }
}
