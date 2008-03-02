package org.jaqlib.query.iterable;

import org.jaqlib.query.Query;
import org.jaqlib.query.QueryBuilder;
import org.jaqlib.reflect.JaqlibInvocationRecorder;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class IterableQueryBuilder<T> implements QueryBuilder<T, Iterable<T>>
{

  private final JaqlibInvocationRecorder invocationRecorder;


  public IterableQueryBuilder(JaqlibInvocationRecorder invocationRecorder)
  {
    this.invocationRecorder = Assert.notNull(invocationRecorder);
  }


  @Override
  public Query<T, Iterable<T>> createQuery()
  {
    return new IterableQuery<T>(invocationRecorder);
  }

}
