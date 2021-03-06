package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.core.reflect.RecordingProxy;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public abstract class AbstractComparableWhereCondition<T, DataSourceType, ResultType>
    extends QueryItem<T, DataSourceType> implements WhereCondition<T>,
    ComparableWhereCondition<T, DataSourceType, ResultType>, LoggableQueryItem
{

  protected Compare<T, ResultType> compare;


  public AbstractComparableWhereCondition(Query<T, DataSourceType> query)
  {
    super(query);
  }


  public QueryResult<T, DataSourceType> isNull()
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsNull<>(invocation);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isNotNull()
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsNotNull<>(invocation);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isEqual(ResultType expected)
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsEqual<>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isGreaterThan(ResultType expected)
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsGreaterThan<>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isGreaterThanOrEqualTo(
      ResultType expected)
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsGreaterThanOrEqualTo<>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isSmallerThan(ResultType expected)
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsSmallerThan<>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isSmallerThanOrEqualTo(
      ResultType expected)
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsSmallerThanOrEqualTo<>(invocation,
        expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isNotEqual(ResultType expected)
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsNotEqual<>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isSame(ResultType expected)
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsSame<>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public QueryResult<T, DataSourceType> isNotSame(ResultType expected)
  {
    final MethodInvocation invocation = getCurrentMethodInvocation();
    this.compare = new IsNotSame<>(invocation, expected);
    return getQuery().createQueryResult();
  }


  public boolean evaluate(T element)
  {
    return compare.evaluate(element);
  }


  /**
   * @return the last recorded method invocation on a {@link RecordingProxy}
   *         object. Can return null if no method invocations are supported.
   */
  protected abstract MethodInvocation getCurrentMethodInvocation();


}
