package mikugo.dev.search.helper;

import java.util.List;

import com.liferay.portal.kernel.search.SearchException;

import mikugo.dev.search.model.ResultModel;

public interface Search {
    List<ResultModel> getResult() throws SearchException, Exception;
}
