package org.jaqlib.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jaqlib.core.AbstractQuery;
import org.jaqlib.core.QueryResult;
import org.jaqlib.core.reflect.MethodCallRecorder;
import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T> the result element class of the query.
 */
public class DbQuery<T> extends AbstractQuery<T, DbSelectDataSource>
{

  private final AbstractMapping<T> mapping;
  private DbQueryCache<T> cache;
  private final List<Object> prepStmtParameters = new ArrayList<Object>();


  public DbQuery(MethodCallRecorder methodCallRecorder,
      AbstractMapping<T> mapping)
  {
    super(methodCallRecorder);
    this.mapping = Assert.notNull(mapping);
  }


  @Override
  public QueryResult<T, DbSelectDataSource> createQueryResult()
  {
    return new DbQueryResult<T>(this);
  }


  public DbWhereClause<T> createDbWhereClause(DbSelectDataSource dataSource)
  {
    setDataSource(dataSource);
    return new DbWhereClause<T>(this);
  }


  @Override
  protected <KeyType> void addResults(final Map<KeyType, T> resultMap)
  {
    final MethodInvocation invocation = getCurrentInvocation();
    getCachingFetchStrategy().addResults(resultMap, invocation,
        prepStmtParameters);
  }


  @Override
  protected void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    getFetchStrategy(stopAtFirstMatch).addResults(result, prepStmtParameters);
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
    return initFetchStrategy(new CachingFetchStrategy<T>(getCache()));
  }


  private AbstractFetchStrategy<T> getFirstOccurenceFetchStrategy()
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


  private DbQueryCache<T> getCache()
  {
    if (cache == null)
    {
      cache = new DbQueryCache<T>(tree);
    }
    return cache;
  }


  @Override
  protected String getResultDefinitionString()
  {
    return mapping.getLogString();
  }


  public QueryResult<T, DbSelectDataSource> addPrepStmtParameters(
      Object[] prepStmtParameters)
  {
    if (prepStmtParameters != null)
    {
      for (Object param : prepStmtParameters)
      {
        this.prepStmtParameters.add(param);
      }
    }
    return createQueryResult();
  }

}
