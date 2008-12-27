package org.jaqlib.query;

import org.jaqlib.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public class IsNotNull<T, ResultType> extends ReflectiveCompare<T, ResultType>
{

  public IsNotNull(MethodInvocation invocation)
  {
    super(invocation, null);
  }


  public boolean evaluate(T item)
  {
    ResultType actual = getActualValue(item);
    return actual != null;
  }


}
