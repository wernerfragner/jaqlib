package org.jaqlib.query;

import org.jaqlib.util.reflect.MethodInvocation;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class IsSmallerThan<T, ResultType> extends
    ComparableFunction<T, ResultType>
{

  public IsSmallerThan(MethodInvocation invocation, ResultType expected)
  {
    super(invocation, expected);
  }


  @Override
  protected boolean doCompare(Comparable<ResultType> actual, ResultType expected)
  {
    return actual.compareTo(expected) < 0;
  }


}
