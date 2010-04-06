package org.jaqlib.db;

import org.jaqlib.core.DataSourceQuery;
import org.jaqlib.core.QueryResult;
import org.jaqlib.core.bean.AbstractMapping;
import org.jaqlib.core.reflect.MethodCallRecorder;

/**
 * @author Werner Fragner
 * 
 * @param <T> the result element class of the query.
 */
public class DbQuery<T> extends DataSourceQuery<T, DbSelectDataSource>
{


  public DbQuery(MethodCallRecorder methodCallRecorder,
      AbstractMapping<T> mapping)
  {
    super(methodCallRecorder, mapping);
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
