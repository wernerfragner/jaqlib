package org.jaqlib.query;

import org.jaqlib.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public class IsNotSame<T, ResultType> extends ReflectiveCompare<T, ResultType>
{

  public IsNotSame(MethodInvocation invocation, ResultType expected)
  {
    super(invocation, expected);
  }


  @Override
  public boolean evaluate(T item)
  {
    ResultType actual = getActualValue(item);
    return actual != expected;
  }

}
