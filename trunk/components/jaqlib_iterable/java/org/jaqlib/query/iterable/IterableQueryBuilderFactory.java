package org.jaqlib.query.iterable;

import org.jaqlib.query.QueryBuilder;
import org.jaqlib.query.QueryBuilderFactory;
import org.jaqlib.reflect.JaqlibInvocationRecorder;

/**
 * @author Werner Fragner
 */
public class IterableQueryBuilderFactory implements QueryBuilderFactory
{

  public <T, DataSourceType> QueryBuilder<T, DataSourceType> getQueryBuilder(
      JaqlibInvocationRecorder jaqlibInvocationRecorder)
  {
    return (QueryBuilder<T, DataSourceType>) new IterableQueryBuilder<T>(
        jaqlibInvocationRecorder);
  }

}
