package org.jaqlib.core;

/**
 * Common interface for all sorts of queries (DB, XML, Iterable, ...).
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 *          the element type.
 * @param <DataSourceType>
 *          the data source type (e.g. Iterable, database, ...).
 */
public interface Query<T, DataSourceType> extends
    ResultProvider<T, DataSourceType>
{

  QueryResult<T, DataSourceType> createQueryResult();


  <R> SingleElementWhereCondition<T, DataSourceType, R> addElementAndWhereCondition();


  <R> SingleElementWhereCondition<T, DataSourceType, R> addElementOrWhereCondition();


  QueryResult<T, DataSourceType> addAndWhereCondition(
      WhereCondition<? super T> condition);


  QueryResult<T, DataSourceType> addOrWhereCondition(
      WhereCondition<? super T> condition);


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveAndWhereCondition();


  <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveOrWhereCondition();


  QueryResult<T, DataSourceType> addTask(Task<? super T> task);


  void addTaskAndExecute(Task<? super T> task);


  int count();


  int countDistinct();

}
