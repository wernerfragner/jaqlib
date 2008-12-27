package org.jaqlib.query;

import org.jaqlib.reflect.JaqlibProxy;
import org.jaqlib.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public abstract class AbstractComparableWhereCondition<T, DataSourceType, ResultType>
    extends QueryItem<T, DataSourceType> implements WhereCondition<T>,
    ComparableWhereCondition<T, DataSourceType, ResultType>
{

  private Compare<T> compare;


  public AbstractComparableWhereCondition(Query<T, DataSourceType> query)
  {
    super(query);
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


  public boolean evaluate(T item)
  {
    return compare.evaluate(item);
  }


  /**
   * @return the last recorded method invocation on a {@link JaqlibProxy}
   *         object. Can return null if no method invocations are supported.
   */
  protected abstract MethodInvocation getLastMethodInvocation();

}
