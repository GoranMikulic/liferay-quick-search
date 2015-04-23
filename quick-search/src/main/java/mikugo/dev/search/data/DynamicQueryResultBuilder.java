package mikugo.dev.search.data;

import java.util.List;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.theme.ThemeDisplay;

public class DynamicQueryResultBuilder {

    private String className;
    private int maxSearchResults;
    private ThemeDisplay themeDisplay;
    
    private String pattern;

    public DynamicQueryResultBuilder(String className, int maxSearchResults, ThemeDisplay themeDisplay, String pattern)
	    throws Exception {
	
	this.className = className;
	this.maxSearchResults = maxSearchResults;
	this.themeDisplay = themeDisplay;
	
	this.pattern = pattern;
    }

    public List<ResultModel> getResult() throws Exception {

	if (className.equals(AssetTypes.SITE.getClassName())) {
	    
	    return new GroupSearcherImpl(this.pattern, this.maxSearchResults, this.themeDisplay).getResult();
	    
	} else if (className.equals(AssetTypes.LAYOUT.getClassName())) {
	    
	    return new LayoutSearchImpl(this.pattern, this.maxSearchResults, this.themeDisplay).getResult();
	}
	return null;
    }

    
}
