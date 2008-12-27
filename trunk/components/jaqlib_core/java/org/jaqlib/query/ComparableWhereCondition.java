package org.jaqlib.query;


public interface ComparableWhereCondition<T, DataSourceType, ResultType>
{

  QueryResult<T, DataSourceType> isNull();


  QueryResult<T, DataSourceType> isNotNull();


  QueryResult<T, DataSourceType> isEqual(ResultType expected);


  QueryResult<T, DataSourceType> isGreaterThan(T expected);


  QueryResult<T, DataSourceType> isGreaterThanOrEqualTo(T expected);


  QueryResult<T, DataSourceType> isSmallerThan(T expected);


  QueryResult<T, DataSourceType> isSmallerThanOrEqualTo(T expected);


  QueryResult<T, DataSourceType> isNotEqual(ResultType expected);


  QueryResult<T, DataSourceType> isSame(ResultType expected);


  QueryResult<T, DataSourceType> isNotSame(ResultType expected);

}