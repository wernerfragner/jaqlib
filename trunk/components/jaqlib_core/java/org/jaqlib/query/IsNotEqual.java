package org.jaqlib.query;

import org.jaqlib.reflect.MethodInvocation;
import org.jaqlib.util.CompareUtil;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public class IsNotEqual<T, ResultType> extends ReflectiveCompare<T, ResultType>
{

  public IsNotEqual(MethodInvocation invocation, ResultType expected)
  {
    super(invocation, expected);
  }


  @Override
  public boolean evaluate(T item)
  {
    ResultType actual = getActualValue(item);
    return !CompareUtil.areEqual(actual, expected);
  }

}
