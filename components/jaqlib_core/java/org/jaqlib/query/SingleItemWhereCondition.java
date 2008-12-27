package org.jaqlib.query;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public interface SingleItemWhereCondition<T, DataSourceType, ResultType>
{

  /**
   * This method represents one single item of the data source.
   * 
   * @return an object for defining the condition.
   */
  ComparableWhereCondition<T, DataSourceType, ResultType> item();

}