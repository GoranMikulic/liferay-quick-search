package mikugo.dev.search.query;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.theme.ThemeDisplay;

public class GroupMapper implements QueryResultMapper<Group> {

    private ThemeDisplay themeDisplay;

    private static Log log = LogFactoryUtil.getLog(GroupMapper.class);

    public GroupMapper(ThemeDisplay themeDisplay) {
	this.themeDisplay = themeDisplay;
    }

    @Override
    public ResultModel getResultModel(Group group) {
	try {
	    return new ResultModel(group.getDescriptiveName(), group.getDescription(), getGroupUrl(group),
		    LanguageUtil.get(themeDisplay.getLocale(), AssetTypes.SITE.getReadableName()), "");
	} catch (PortalException e) {
	    log.error(e);
	} catch (SystemException e) {
	    log.error(e);
	}

	return null;
    }

    /**
     * Returns landing page url of a {@link Group}
     * 
     * @param group
     * @return Landing page url of a {@link Group}
     */
    private String getGroupUrl(Group group) {
	if (group.isControlPanel() || !group.hasPublicLayouts()) {
	    return "/group" + group.getFriendlyURL();
	} else {
	    return "/web" + group.getFriendlyURL();
	}
    }
}
