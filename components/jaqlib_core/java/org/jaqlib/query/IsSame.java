package org.jaqlib.query;

import org.jaqlib.util.reflect.MethodInvocation;

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


  @Override
  public boolean doEvaluate(ResultType actual)
  {
    return actual == expected;
  }

}
