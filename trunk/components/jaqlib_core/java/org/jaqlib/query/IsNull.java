package org.jaqlib.query;

import org.jaqlib.reflect.MethodInvocation;

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
  public boolean evaluate(T item)
  {
    // if no invocation is present then item itself should be checked for null
    if (!invocationPresent())
    {
      return (item == null);
    }

    // invocation present but item itself is null; item cannot be evaluated
    if (item == null)
    {
      return false;
    }

    // invoke recored method and check for null
    ResultType actual = getActualValue(item);
    return actual == null;
  }


}
