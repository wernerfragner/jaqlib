package org.jaqlib.core;


/**
 * @author Werner Fragner
 * 
 * @param <T> the element type.
 * @param <DataSourceType> the data source type (e.g. Iterable, database, ...).
 */
public interface Query<T, DataSourceType> extends
    ResultProvider<T, DataSourceType>
{

  WhereClause<T, DataSourceType> createWhereClause(DataSourceType dataSource);


  QueryResult<T, DataSourceType> createQueryResult();


  <R> SingleElementWhereCondition<T, DataSourceType, R> addElementAndWhereCondition();


  <R> SingleElementWhereCondition<T, DataSourceType, R> addElementOrWhereCondition();


  QueryResult<T, DataSourceType> addAndWhereCondition(
      WhereCondition<T> condition);


  QueryResult<T, DataSourceType> addOrWhereCondition(WhereCondition<T> condition);


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveAndWhereCondition();


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveOrWhereCondition();


  QueryResult<T, DataSourceType> addTask(Task<T> task);


  void addTaskAndExecute(Task<T> task);

}
