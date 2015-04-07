package mikugo.dev.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

public class Utils {

    public static final String MODEL_JOURNAL_ARTICLE = "com.liferay.portlet.journal.model.JournalArticle";
    public static final String MODEL_USER = "com.liferay.portal.model.User";
    public static final String MODEL_FILE = "com.liferay.portlet.documentlibrary.model.DLFileEntry";
    public static final String MODEL_GROUP = "com.liferay.portal.model.Group";

    public static ThemeDisplay getThemeDisplay(PortletRequest request) {
	if (null == request) {
	    throw new IllegalArgumentException("request is null");
	}

	return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
    }

}
