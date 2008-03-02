package org.jaqlib.query;

import org.jaqlib.reflect.JaqlibInvocationRecorder;
import org.jaqlib.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public class ReflectiveWhereCondition<T, DataSourceType, ResultType> extends
    QueryItem<T, DataSourceType> implements WhereCondition<T>
{

  private final JaqlibInvocationRecorder invocationRecorder;
  private Compare<T> compare;


  public ReflectiveWhereCondition(Query<T, DataSourceType> query,
      JaqlibInvocationRecorder invocationRecorder)
  {
    super(query);
    this.invocationRecorder = Assert.notNull(invocationRecorder);
  }


  public QueryResult<T, DataSourceType> isNull()
  {
    final MethodInvocation invocation = getLastMethodInvocation();
    this.compare = new IsNull<T, ResultType>(invocation);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isNotNull()
  {
    final MethodInvocation invocation = getLastMethodInvocation();
    this.compare = new IsNotNull<T, ResultType>(invocation);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isEqual(ResultType expected)
  {
    final MethodInvocation invocation = getLastMethodInvocation();
    this.compare = new IsEqual<T, ResultType>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isGreaterThan(T expected)
  {
    this.compare = new IsGreaterThan<T>(expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isGreaterThanOrEqualTo(T expected)
  {
    this.compare = new IsGreaterThanOrEqualTo<T>(expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isSmallerThan(T expected)
  {
    this.compare = new IsSmallerThan<T>(expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isSmallerThanOrEqualTo(T expected)
  {
    this.compare = new IsSmallerThanOrEqualTo<T>(expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isNotEqual(ResultType expected)
  {
    final MethodInvocation invocation = getLastMethodInvocation();
    this.compare = new IsNotEqual<T, ResultType>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isSame(ResultType expected)
  {
    final MethodInvocation invocation = getLastMethodInvocation();
    this.compare = new IsSame<T, ResultType>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isNotSame(ResultType expected)
  {
    final MethodInvocation invocation = getLastMethodInvocation();
    this.compare = new IsNotSame<T, ResultType>(invocation, expected);
    return getQuery().createQueryResult();
  }


  @Override
  public boolean evaluate(T item)
  {
    return compare.evaluate(item);
  }


  private MethodInvocation getLastMethodInvocation()
  {
    return invocationRecorder.getLastInvocation();
  }


}
