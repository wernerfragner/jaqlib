package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public class IsNull<T, ResultType> extends ReflectiveCompare<T, ResultType>
{

  public IsNull(MethodInvocation invocation)
  {
    super(invocation, null);
  }


  @Override
  public boolean evaluate(T element)
  {
    // if no invocation is present then element itself should be checked for
    // null
    if (!invocationPresent())
    {
      return (element == null);
    }

    // invocation present but element itself is null; element cannot be
    // evaluated
    if (element == null)
    {
      return false;
    }

    return doEvaluate(getActualValue(element));
  }


  @Override
  protected boolean doEvaluate(ResultType actual)
  {
    return actual == null;
  }

}
