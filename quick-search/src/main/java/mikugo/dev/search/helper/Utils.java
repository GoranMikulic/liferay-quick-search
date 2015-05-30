package mikugo.dev.search.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Util Class for quick search project
 * 
 * @author Goran Mikulic
 *
 */
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

    /**
     * Removing dynamic query class names from the configured asset types to get
     * the configured types for index search
     * 
     * @param assetTypes
     * 
     * @return
     */
    public static String[] filterIndexTypes(String[] assetTypes) {

	List<String> filteredIndexTypes = new ArrayList<String>();

	for (String dynQueryClassName : AssetTypes.getIndexSearchClassNames()) {
	    if (ArrayUtil.contains(assetTypes, dynQueryClassName)) {
		filteredIndexTypes.add(dynQueryClassName);
	    }
	}
	
	return filteredIndexTypes.toArray(new String[filteredIndexTypes.size()]);
    }
    
    /**
     * Checks if the search keyword contains the pattern <type>:<keyword>
     * 
     * @param pattern
     * @return
     */
    public static boolean isFaceted(String pattern) {
	// Regex should search for following pattern <type>:<keyword>
	Pattern regex = Pattern.compile("([A-Z,a-z])+\\w:((\\s)*([A-Z,a-z])*)*");
	Matcher matcher = regex.matcher(pattern);
	return matcher.matches();
    }

}
