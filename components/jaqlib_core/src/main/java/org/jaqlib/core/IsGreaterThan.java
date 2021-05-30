package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class IsGreaterThan<T, ResultType> extends
    ComparableFunction<T, ResultType>
{

  public IsGreaterThan(MethodInvocation invocation, ResultType expected)
  {
    super(invocation, expected);
  }


  @Override
  protected boolean doCompare(Comparable<ResultType> actual, ResultType expected)
  {
    return actual.compareTo(expected) > 0;
  }

}
