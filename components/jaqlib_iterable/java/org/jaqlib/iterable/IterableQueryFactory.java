package org.jaqlib.iterable;

import org.jaqlib.core.AbstractQueryFactory;
import org.jaqlib.core.reflect.MethodCallRecorder;


/**
 * Creates {@link IterableQuery} objects.
 * 
 * @author Werner Fragner
 */
public class IterableQueryFactory extends AbstractQueryFactory
{

  public IterableQueryFactory(MethodCallRecorder methodCallRecorder)
  {
    super(methodCallRecorder);
  }


  /**
   * @param <T> the element type of the {@link Iterable}.
   * @return a query for using the functionality of JaQLib without the fluent
   *         API.
   */
  public <T> IterableQuery<T> createQuery()
  {
    return new IterableQuery<T>(methodCallRecorder);
  }

}
