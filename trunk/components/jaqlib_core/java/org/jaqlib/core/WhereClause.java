package org.jaqlib.core;


/**
 * Represents the WHERE clause of the query.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 */
public class WhereClause<T, DataSourceType> extends
    QueryResult<T, DataSourceType>
{

  public WhereClause(Query<T, DataSourceType> query)
  {
    super(query);
  }


  /**
   * Simple WHERE clause that can be used to test all elements of the data
   * source for a specific condition.
   * 
   * @param <R> the result element type.
   * @return an object that represents a WHERE condition on a single element of
   *         the data source.
   */
  public <R> SingleElementWhereCondition<T, DataSourceType, R> where()
  {
    return getQuery().addSimpleWhereCondition();
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
  public <R> ComparableWhereCondition<T, DataSourceType, R> where(R evalResult)
  {
    return getQuery().addReflectiveWhereCondition();
  }


  /**
   * WHERE clause with a custom condition.
   * 
   * @param condition a not null condition.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public QueryResult<T, DataSourceType> where(WhereCondition<T> condition)
  {
    return getQuery().addWhereCondition(condition);
  }

}
