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

  WhereClause<T, DataSourceType> createWhereClause(DataSourceType dataSource);


  QueryResult<T, DataSourceType> createQueryResult();


  <R> SingleElementWhereCondition<T, DataSourceType, R> addSimpleWhereCondition();


  <R> SingleElementWhereCondition<T, DataSourceType, R> addSimpleAndWhereCondition();


  <R> SingleElementWhereCondition<T, DataSourceType, R> addSimpleOrWhereCondition();


  QueryResult<T, DataSourceType> addWhereCondition(WhereCondition<T> condition);


  QueryResult<T, DataSourceType> addAndWhereCondition(
      WhereCondition<T> condition);


  QueryResult<T, DataSourceType> addOrWhereCondition(WhereCondition<T> condition);


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveWhereCondition();


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveAndWhereCondition();


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveOrWhereCondition();

}
