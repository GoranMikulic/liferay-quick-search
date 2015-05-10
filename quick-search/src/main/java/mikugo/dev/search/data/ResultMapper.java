package mikugo.dev.search.data;

import mikugo.dev.search.model.ResultModel;

/**
 * Interface for Mapper classes, which are mapping results from internal types
 * to {@link ResultModel}
 * 
 * @author mikugo
 *
 */
public interface ResultMapper {
    ResultModel getResultModel();
}
