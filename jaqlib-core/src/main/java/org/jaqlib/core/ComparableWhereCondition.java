package org.jaqlib.core;

/**
 * @author Werner Fragner
 * 
 * @param <T> the element type.
 * @param <DataSourceType> the data source type.
 * @param <ResultType> the type to which the condition should be evaluated
 *          against.
 */
public interface ComparableWhereCondition<T, DataSourceType, ResultType>
{

  /**
   * Defines the condition that the result of a recorded method call must be
   * null.
   * 
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isNull();


  /**
   * Defines the condition that the result of a recorded method call must not be
   * null.
   * 
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isNotNull();


  /**
   * Defines the condition that the result of a recorded method call must be
   * equal to the given value.
   * 
   * @param expected the value that should match the result of the recorded
   *          method call.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isEqual(ResultType expected);


  /**
   * Defines the condition that the result of a recorded method call must be not
   * equal to the given value.
   * 
   * @param expected the value that should not match the result of the recorded
   *          method call.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isNotEqual(ResultType expected);


  /**
   * Defines the condition that the result of a recorded method call must be the
   * same as the given value (regarding object identity).
   * 
   * @param expected the value that should match the result of the recorded
   *          method call.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isSame(ResultType expected);


  /**
   * Defines the condition that the result of a recorded method call must not be
   * the same as the given value (regarding object identity).
   * 
   * @param expected the value that should not match the result of the recorded
   *          method call.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isNotSame(ResultType expected);


  /**
   * Defines the condition that the result of a recorded method call must be
   * greater than the given value.
   * 
   * @param expected the value that should be smaller than or equal to the
   *          result of the recorded method call.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isGreaterThan(ResultType expected);


  /**
   * Defines the condition that the result of a recorded method call must be
   * greater than or equal to the given value.
   * 
   * @param expected the value that should be smaller than the result of the
   *          recorded method call.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isGreaterThanOrEqualTo(ResultType expected);


  /**
   * Defines the condition that the result of a recorded method call must be
   * smaller than the given value.
   * 
   * @param expected the value that should be greater than or equal to the
   *          result of the recorded method call.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isSmallerThan(ResultType expected);


  /**
   * Defines the condition that the result of a recorded method call must be
   * smaller than or equal to the given value.
   * 
   * @param expected the value that should be greater than the result of the
   *          recorded method call.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  QueryResult<T, DataSourceType> isSmallerThanOrEqualTo(ResultType expected);


}