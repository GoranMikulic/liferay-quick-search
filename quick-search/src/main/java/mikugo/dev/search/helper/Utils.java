package mikugo.dev.search.helper;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

public class Utils {
    
    public static final String CONFIGURATION_KEY_CONTROLLED = "keyControlled";
    public static final String CONFIGURATION_MIN_SEARCH_LETTERS = "minSearchLetters";
    public static final String CONFIGURATION_ASSET_TYPES = "assetTypes";
    public static final String CONFIGURATION_MAXIMUM_SEARCH_RESULTS = "maximumSearchResults";

    public static ThemeDisplay getThemeDisplay(PortletRequest request) {
	if (null == request) {
	    throw new IllegalArgumentException("request is null");
	}

	return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
    }

}
