package org.jaqlib.db;

import java.util.Collection;
import java.util.Map;

import org.jaqlib.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * Fetch strategy that uses a cache to avoid unnecessary database calls.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the element type of the query.
 */
public class OptimizedFetchStrategy<T> extends AbstractFetchStrategy<T>
{

  private final DatabaseQueryCache<T> cache;


  public OptimizedFetchStrategy(DatabaseQueryCache<T> cache)
  {
    this.cache = Assert.notNull(cache);
  }


  @Override
  public void addResults(Collection<T> result)
  {
    if (cache.isFilled())
    {
      cache.addResults(result);
    }
    else
    {
      super.addResults(result);
      cache.setFilled();
    }
  }


  @Override
  public <KeyType> void addResults(Map<KeyType, T> resultMap,
      MethodInvocation invocation)
  {
    if (cache.isFilled())
    {
      cache.addResults(resultMap, invocation);
    }
    else
    {
      super.addResults(resultMap, invocation);
      cache.setFilled();
    }
  }


  /**
   * All records must be processed. So this method always returns false.
   */
  @Override
  protected boolean recordProcessed(T element, boolean isMatch)
  {
    cache.add(element);
    return false;
  }

}
