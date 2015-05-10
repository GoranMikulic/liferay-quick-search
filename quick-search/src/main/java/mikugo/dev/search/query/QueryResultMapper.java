package mikugo.dev.search.query;

import mikugo.dev.search.model.ResultModel;

public interface QueryResultMapper<T> {
    ResultModel getResultModel(T type);
}
