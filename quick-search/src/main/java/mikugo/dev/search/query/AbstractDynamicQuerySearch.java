package mikugo.dev.search.query;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Abstract class for a search via Liferay {@link DynamicQuery} API
 * 
 * @author mikugo
 *
 */
public abstract class AbstractDynamicQuerySearch {

    protected String pattern;

    protected int maxSearchResults;

    protected ThemeDisplay themeDisplay;

    public AbstractDynamicQuerySearch(String pattern, int maxSearchResults, ThemeDisplay themeDisplay) {
	this.pattern = pattern;
	this.maxSearchResults = maxSearchResults;
	this.themeDisplay = themeDisplay;
    }
}
