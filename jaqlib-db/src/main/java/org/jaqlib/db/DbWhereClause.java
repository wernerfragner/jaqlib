package org.jaqlib.db;

import org.jaqlib.core.ComparableWhereCondition;
import org.jaqlib.core.SingleElementWhereCondition;
import org.jaqlib.core.WhereCondition;


/**
 * Represents the WHERE clause of the query.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class DbWhereClause<T> extends DbQueryResult<T>
{

  public DbWhereClause(DbQuery<T> query)
  {
    super(query);
  }


  /**
   * Simple WHERE clause that can be used to test all elements of the data
   * source for a specific condition.
   * 
   * @param <ResultType> the result element type.
   * @return an object that represents a WHERE condition on a single element of
   *         the data source.
   */
  public <ResultType> SingleElementWhereCondition<T, DbSelectDataSource, ResultType> where()
  {
    return getQuery().addElementAndWhereCondition();
  }


  /**
   * Shortcut method for <tt>where().element()</tt>.
   * 
   * @param <ResultType> the result element type.
   * @return an object to specify the condition on an element.
   */
  public <ResultType> ComparableWhereCondition<T, DbSelectDataSource, ResultType> whereElement()
  {
    return this.<ResultType> where().element();
  }


  /**
   * Uses a recorded method call to test all elements for a specific condition.
   * This condition can be specified in the returned
   * {@link ComparableWhereCondition}.
   * 
   * @param evalResult the result of the recorded method call. This result is
   *          only needed for type safety. The object itself is not used.
   * @return an object to specify the condition.
   */
  public <R> ComparableWhereCondition<T, DbSelectDataSource, R> whereCall(
      R evalResult)
  {
    return getQuery().addReflectiveAndWhereCondition();
  }


  /**
   * Uses a recored method call to test all elements for a boolean condition.
   * The condition must return true in order to add the element to the result
   * set.
   * 
   * @param evalResult the result of the recorded method call. This result is
   *          only needed for type safety. The object itself is not used.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public <R> DbQueryResult<T> whereCallIsTrue(Boolean evalResult)
  {
    return (DbQueryResult<T>) getDatabaseQuery()
        .addReflectiveAndWhereCondition().isEqual(true);
  }


  /**
   * Uses a recored method call to test all elements for a boolean condition.
   * The condition must return false in order to add the element to the result
   * set.
   * 
   * @param evalResult the result of the recorded method call. This result is
   *          only needed for type safety. The object itself is not used.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public <R> DbQueryResult<T> whereCallIsFalse(Boolean evalResult)
  {
    return (DbQueryResult<T>) getDatabaseQuery()
        .addReflectiveAndWhereCondition().isEqual(false);
  }


  /**
   * WHERE clause with a custom condition.
   * 
   * @param condition a not null condition.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public DbQueryResult<T> where(WhereCondition<? super T> condition)
  {
    return (DbQueryResult<T>) getDatabaseQuery()
        .addAndWhereCondition(condition);
  }


  /**
   * WHERE clause with a custom SQL WHERE condition (without the WHERE clause).
   * 
   * @param sqlWhereCondition a not null SQL WHERE condition.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public DbQueryResult<T> where(String sqlWhereCondition)
  {
    return (DbQueryResult<T>) getDatabaseQuery().addAndWhereCondition(
        sqlWhereCondition);
  }

}
