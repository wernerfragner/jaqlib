package org.jaqlib.db;

import org.jaqlib.core.QueryResult;

import java.sql.PreparedStatement;


/**
 * Provides methods to return the result of a database query. It also provides
 * methods to add additional WHERE conditions.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class DbQueryResult<T> extends QueryResult<T, DbSelectDataSource>
{

  public DbQueryResult(DbQuery<T> query)
  {
    super(query);
  }


  /**
   * <p>
   * When using this functionality then a {@link PreparedStatement} is used for
   * executing the SELECT statement. {@link PreparedStatement}s can degrade
   * query performance if they are not executed many times. So if performance is
   * an issue then {@link PreparedStatement}s should not be used when only a
   * single statement is executed.
   * </p>
   * Uses the given parameters to fill the prepared SELECT statement. The order
   * in which the parameters are inserted into the {@link PreparedStatement} is
   * the same as the order in which they are given into this method.
   * 
   * @param prepStmtParameters the parameters for the prepared SELECT statement.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public QueryResult<T, DbSelectDataSource> using(Object... prepStmtParameters)
  {
    return getDatabaseQuery().addPrepStmtParameters(prepStmtParameters);
  }


  protected DbQuery<T> getDatabaseQuery()
  {
    // this cast is valid because only a DatabaseQuery can be set in the
    // constructor
    return (DbQuery<T>) getQuery();
  }


}
