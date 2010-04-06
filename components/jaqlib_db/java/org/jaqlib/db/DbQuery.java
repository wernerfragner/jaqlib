package org.jaqlib.db;

import java.util.Collection;
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
        getDataSource().addPreparedStatementParameter(param);
      }
    }

    return createQueryResult();
  }


  public QueryResult<T, DbSelectDataSource> addAndWhereCondition(
      String sqlWhereCondition)
  {
    getDataSource().setSqlWhereCondition(sqlWhereCondition);
    return createQueryResult();
  }
}
