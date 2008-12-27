package org.jaqlib.query.iterable;

import org.jaqlib.query.Query;
import org.jaqlib.query.QueryBuilder;
import org.jaqlib.reflect.MethodCallRecorder;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class IterableQueryBuilder<T> implements QueryBuilder<T, Iterable<T>>
{

  private final MethodCallRecorder invocationRecorder;


  public IterableQueryBuilder(MethodCallRecorder invocationRecorder)
  {
    this.invocationRecorder = Assert.notNull(invocationRecorder);
  }


  public Query<T, Iterable<T>> createQuery()
  {
    return new IterableQuery<T>(invocationRecorder);
  }

}
