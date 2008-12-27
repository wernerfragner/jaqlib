package org.jaqlib.query;


public interface ComparableWhereCondition<T, DataSourceType, ResultType>
{

  QueryResult<T, DataSourceType> isNull();


  QueryResult<T, DataSourceType> isNotNull();


  QueryResult<T, DataSourceType> isEqual(ResultType expected);


  QueryResult<T, DataSourceType> isGreaterThan(ResultType expected);


  QueryResult<T, DataSourceType> isGreaterThanOrEqualTo(ResultType expected);


  QueryResult<T, DataSourceType> isSmallerThan(ResultType expected);


  QueryResult<T, DataSourceType> isSmallerThanOrEqualTo(ResultType expected);


  QueryResult<T, DataSourceType> isNotEqual(ResultType expected);


  QueryResult<T, DataSourceType> isSame(ResultType expected);


  QueryResult<T, DataSourceType> isNotSame(ResultType expected);

}