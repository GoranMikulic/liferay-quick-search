package mikugo.dev.search.data;

import com.liferay.portal.theme.ThemeDisplay;

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
