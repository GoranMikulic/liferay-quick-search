package mikugo.dev.search.query;

import java.util.List;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.theme.ThemeDisplay;

/**
 * Proxy class which delegates search for different dynamic query search types
 * 
 * @author Goran Mikulic
 *
 */
public class DynamicQueryResultFactory {

    private String className;
    private int maxSearchResults;
    private ThemeDisplay themeDisplay;

    private String pattern;

    /**
     * Constructor
     */
    public DynamicQueryResultFactory(String className, int maxSearchResults, ThemeDisplay themeDisplay, String pattern)
	    throws Exception {

	this.className = className;
	this.maxSearchResults = maxSearchResults;
	this.themeDisplay = themeDisplay;
	this.pattern = pattern;
    }

    /**
     * Returs a list of {@link ResultModel} from the asset types with the given className
     * 
     * @return A list of {@link ResultModel}
     * @throws Exception
     */
    public List<ResultModel> getResult() throws Exception {

	if (className.equals(AssetTypes.SITE.getClassName())) {

	    return new GroupSearcherImpl(this.pattern, this.maxSearchResults, this.themeDisplay).getResult();

	} else if (className.equals(AssetTypes.LAYOUT.getClassName())) {

	    return new LayoutSearchImpl(this.pattern, this.maxSearchResults, this.themeDisplay).getResult();
	}

	return null;
    }

}
