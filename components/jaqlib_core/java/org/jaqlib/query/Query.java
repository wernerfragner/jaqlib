package org.jaqlib.query;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 */
public interface Query<T, DataSourceType> extends
    ResultProvider<T, DataSourceType>
{

  /**
   * Create a from clause with only one element in the select clause (= only one
   * result element class).
   * 
   * @param resultElementClass
   * @return an object representing a from clause.
   */
  FromClause<T, DataSourceType> createFromClause(Class<T> resultElementClass);


  WhereClause<T, DataSourceType> createWhereClause(DataSourceType dataSource);


  QueryResult<T, DataSourceType> createQueryResult();


  <R> SingleElementWhereCondition<T, DataSourceType, R> addSimpleWhereCondition();


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveWhereCondition();


  QueryResult<T, DataSourceType> addWhereCondition(WhereCondition<T> condition);


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveAndWhereCondition();


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveOrWhereCondition();


  QueryResult<T, DataSourceType> addAndWhereCondition(
      WhereCondition<T> condition);


  QueryResult<T, DataSourceType> addOrWhereCondition(WhereCondition<T> condition);

}
