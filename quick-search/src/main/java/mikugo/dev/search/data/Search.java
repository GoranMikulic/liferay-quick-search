package mikugo.dev.search.data;

import java.util.List;

import com.liferay.portal.kernel.search.SearchException;

import mikugo.dev.search.model.ResultModel;

/**
 * Interface for search implementations
 * 
 * @author Goran Mikulic
 *
 */
public interface Search {
    List<ResultModel> getResult() throws SearchException, Exception;
}
