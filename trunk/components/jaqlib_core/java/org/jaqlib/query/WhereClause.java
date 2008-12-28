package org.jaqlib.query;


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
   * Simple where clause that can be used to test all elements of the data
   * source for various conditions.
   * 
   * @param <R> the result element type.
   * @return an object that represents a where condition on a single element of
   *         the data source.
   */
  public <R> SingleElementWhereCondition<T, DataSourceType, R> where()
  {
    return getQuery().addSimpleWhereCondition();
  }


  /**
   * Uses a recorded method call to test all elements for a specific condition.
   * This condition can be specified in the returned
   * {@link ComparableWhereCondition}. The condition is added to the query by
   * using a AND connector.
   * 
   * @param evalResult the result of the recorded method call.
   * @return an object to specify the condition.
   */
  public <R> ComparableWhereCondition<T, DataSourceType, R> where(R evalResult)
  {
    return getQuery().addReflectiveWhereCondition();
  }


  /**
   * Where clause with user a defined condition.
   * 
   * @param condition a not null condition.
   * @return the result of the query (including methods to add other where
   *         conditions).
   */
  public QueryResult<T, DataSourceType> where(WhereCondition<T> condition)
  {
    return getQuery().addWhereCondition(condition);
  }

}
