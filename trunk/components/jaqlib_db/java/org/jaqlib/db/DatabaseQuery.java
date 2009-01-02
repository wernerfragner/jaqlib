package org.jaqlib.db;

import java.util.Collection;
import java.util.Map;

import org.jaqlib.core.AbstractQuery;
import org.jaqlib.core.FromClause;
import org.jaqlib.core.reflect.MethodCallRecorder;
import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T> the result element class of the query.
 */
public class DatabaseQuery<T> extends AbstractQuery<T, DbSelectDataSource>
{

  private AbstractMapping<T> mapping;
  private DatabaseQueryCache<T> cache;


  public DatabaseQuery(MethodCallRecorder methodCallRecorder)
  {
    super(methodCallRecorder);
  }


  public FromClause<T, DbSelectDataSource> createFromClause(
      AbstractMapping<T> mapping)
  {
    this.mapping = Assert.notNull(mapping);
    return new FromClause<T, DbSelectDataSource>(this);
  }


  @Override
  protected <KeyType> void addResults(final Map<KeyType, T> resultMap)
  {
    final MethodInvocation invocation = getCurrentInvocation();
    getOptimizedFetchStrategy().addResults(resultMap, invocation);
  }


  @Override
  protected void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    getFetchStrategy(stopAtFirstMatch).addResults(result);
  }


  private AbstractFetchStrategy<T> getFetchStrategy(boolean stopAtFirstMatch)
  {
    if (stopAtFirstMatch)
    {
      return getSimpleFetchStrategy();
    }
    else
    {
      return getOptimizedFetchStrategy();
    }
  }


  private AbstractFetchStrategy<T> getOptimizedFetchStrategy()
  {
    return initFetchStrategy(new CachingFetchStrategy<T>(getCache()));
  }


  private AbstractFetchStrategy<T> getSimpleFetchStrategy()
  {
    return initFetchStrategy(new FirstOccurrenceFetchStrategy<T>());
  }


  private AbstractFetchStrategy<T> initFetchStrategy(
      AbstractFetchStrategy<T> strategy)
  {
    strategy.setDataSource(getDataSource());
    strategy.setMapping(mapping);
    strategy.setPredicate(tree);
    return strategy;
  }


  private DatabaseQueryCache<T> getCache()
  {
    if (cache == null)
    {
      cache = new DatabaseQueryCache<T>(tree);
    }
    return cache;
  }

}
