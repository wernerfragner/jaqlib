package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.Assert;
import org.jaqlib.util.LogUtil;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Fetch strategy that uses a cache to avoid unnecessary calls to the data
 * source.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the element type of the query.
 */
public class CachingFetchStrategy<T> extends AbstractFetchStrategy<T>
{

  private final Logger log = LogUtil.getLogger(this);
  private final QueryCache<T> cache;


  public CachingFetchStrategy(QueryCache<T> cache)
  {
    this.cache = Assert.notNull(cache);
  }


  @Override
  public void addResults(Collection<T> result)
  {
    if (cache.isFilled())
    {
      log.fine("Fetching query results from cache.");

      cache.addResults(result);
    }
    else
    {
      log.fine("Fetching query results from data source.");

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
      log.fine("Fetching query results from cache.");

      cache.addResults(resultMap, invocation);
    }
    else
    {
      log.fine("Fetching query results from data source.");

      super.addResults(resultMap, invocation);
      cache.setFilled();
    }
  }


  /**
   * All records must be processed. So this method always returns false.
   */
  @Override
  protected boolean elementProcessed(T element, boolean isMatch)
  {
    cache.add(element);
    return false;
  }

}
