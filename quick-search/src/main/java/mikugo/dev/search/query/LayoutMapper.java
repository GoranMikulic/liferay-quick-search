package mikugo.dev.search.query;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.ThemeDisplay;

public class LayoutMapper implements QueryResultMapper<Layout> {

    private ThemeDisplay themeDisplay;

    private static Log log = LogFactoryUtil.getLog(LayoutMapper.class);

    public LayoutMapper(ThemeDisplay themeDisplay) {
	this.themeDisplay = themeDisplay;
    }

    @Override
    public ResultModel getResultModel(Layout layout) {

	String layoutUrlPrefix = layout.isPrivateLayout() ? "/group" : "/web";

	try {
	    return new ResultModel(layout.getName(), layout.getDescription(), layoutUrlPrefix
		    + layout.getGroup().getFriendlyURL() + layout.getFriendlyURL(), LanguageUtil.get(
		    themeDisplay.getLocale(), AssetTypes.LAYOUT.getReadableName()), layout.getGroup()
		    .getDescriptiveName());
	} catch (PortalException e) {
	    log.error(e);
	} catch (SystemException e) {
	    log.error(e);
	}

	return null;
    }
}
