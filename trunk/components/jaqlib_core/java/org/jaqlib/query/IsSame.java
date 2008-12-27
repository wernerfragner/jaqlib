package org.jaqlib.query;

import org.jaqlib.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public class IsSame<T, ResultType> extends ReflectiveCompare<T, ResultType>
{

  public IsSame(MethodInvocation invocation, ResultType expected)
  {
    super(invocation, expected);
  }


  public boolean evaluate(T item)
  {
    ResultType actual = getActualValue(item);
    return actual == expected;
  }

}
