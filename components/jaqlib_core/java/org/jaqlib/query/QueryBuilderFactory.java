package org.jaqlib.query;

import org.jaqlib.reflect.JaqlibInvocationRecorder;

/**
 * @author Werner Fragner
 */
public interface QueryBuilderFactory
{

  <T, DataSourceType> QueryBuilder<T, DataSourceType> getQueryBuilder(
      JaqlibInvocationRecorder jaqlibInvocationRecorder);

}
