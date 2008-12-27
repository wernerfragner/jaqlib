package org.jaqlib.query;


/**
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
   * Simple where clause that can be used to test all single items for various
   * conditions.
   * 
   * @param <R> the result item type.
   * @return
   */
  public <R> SingleItemWhereCondition<T, DataSourceType, R> where()
  {
    return getQuery().addSimpleWhereCondition();
  }


  /**
   * Where clause that uses recorded method calls to test all items for various
   * conditions.
   * 
   * @param <R> the result item type.
   * @param evalResult
   * @return
   */
  public <R> ComparableWhereCondition<T, DataSourceType, R> where(R evalResult)
  {
    return getQuery().addReflectiveWhereCondition();
  }


  /**
   * Where clause with user a defined condition.
   * 
   * @param condition
   * @return
   */
  public QueryResult<T, DataSourceType> where(WhereCondition<T> condition)
  {
    return getQuery().addWhereCondition(condition);
  }

}
