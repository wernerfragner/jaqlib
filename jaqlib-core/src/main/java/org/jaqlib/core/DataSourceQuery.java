package org.jaqlib.core;

import org.jaqlib.core.bean.AbstractMapping;
import org.jaqlib.core.reflect.MethodCallRecorder;
import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

import java.util.Collection;
import java.util.Map;


public class DataSourceQuery<T, DataSourceType extends SelectDataSource>
    extends AbstractQuery<T, DataSourceType>
{

  private final AbstractMapping<T> mapping;
  private QueryCache<T> cache;


  public DataSourceQuery(MethodCallRecorder methodCallRecorder,
      AbstractMapping<T> mapping)
  {
    super(methodCallRecorder);
    this.mapping = Assert.notNull(mapping);
  }


  @Override
  protected <KeyType> void addResults(final Map<KeyType, T> resultMap)
  {
    final MethodInvocation invocation = getCurrentInvocation();
    getCachingFetchStrategy().addResults(resultMap, invocation);
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
      return getFirstOccurenceFetchStrategy();
    }
    else
    {
      return getCachingFetchStrategy();
    }
  }


  private AbstractFetchStrategy<T> getCachingFetchStrategy()
  {
    return initFetchStrategy(new CachingFetchStrategy<>(getCache()));
  }


  private AbstractFetchStrategy<T> getFirstOccurenceFetchStrategy()
  {
    return initFetchStrategy(new FirstOccurrenceFetchStrategy<>());
  }


  private AbstractFetchStrategy<T> initFetchStrategy(AbstractFetchStrategy<T> strategy)
  {
    strategy.setDataSource(getDataSource());
    strategy.setMapping(mapping);
    strategy.setPredicate(tree);
    return strategy;
  }


  private QueryCache<T> getCache()
  {
    if (cache == null)
    {
      cache = new QueryCache<>(tree);
    }
    return cache;
  }


  @Override
  protected String getResultDefinitionString()
  {
    return mapping.getLogString();
  }

}
