package org.jaqlib.query;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public interface SingleElementWhereCondition<T, DataSourceType, ResultType>
{

  /**
   * This method represents a where condition on a single element of the data
   * source.
   * 
   * @return an object for defining the condition.
   */
  ComparableWhereCondition<T, DataSourceType, ResultType> element();

}