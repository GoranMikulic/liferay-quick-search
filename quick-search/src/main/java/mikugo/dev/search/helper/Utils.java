package mikugo.dev.search.helper;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

public class Utils {

    public static final String MODEL_JOURNAL_ARTICLE = "com.liferay.portlet.journal.model.JournalArticle";
    public static final String MODEL_USER = "com.liferay.portal.model.User";
    public static final String MODEL_FILE = "com.liferay.portlet.documentlibrary.model.DLFileEntry";
    public static final String MODEL_GROUP = "com.liferay.portal.model.Group";
    public static final String[] MODELS_AVAILABLE = {MODEL_JOURNAL_ARTICLE, MODEL_USER, MODEL_FILE, MODEL_GROUP};
    
    public static final String CONFIGURATION_KEY_CONTROLLED = "keyControlled";
    public static final String CONFIGURATION_MIN_SEARCH_LETTERS = "minSearchLetters";
    public static final String CONFIGURATION_ASSET_TYPES = "assetTypes";

    public static ThemeDisplay getThemeDisplay(PortletRequest request) {
	if (null == request) {
	    throw new IllegalArgumentException("request is null");
	}

	return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
    }

}
