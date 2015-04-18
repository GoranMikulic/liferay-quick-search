package mikugo.dev.search.helper;

import java.util.List;

import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.search.SearchException;

public class DynamicQuerySearcher implements Search {

    private static final String CRITERION_NAME = "name";

    private String className;
    private String pattern;
    private int maxSearchResults;

    public DynamicQuerySearcher(String className, String pattern, int maxSearchResults) {
	this.className = className;
	this.pattern = pattern;
	this.maxSearchResults = maxSearchResults;
    }

    @Override
    public List<ResultModel> getResult() throws SearchException, Exception {
	DynamicQuery query = DynamicQueryFactoryUtil.forClass(Class.forName(this.className)).add(
		PropertyFactoryUtil.forName(CRITERION_NAME).eq(this.pattern));

	return new DynamicQueryResultBuilder(this.className, query, maxSearchResults).getResult();
    }
}
