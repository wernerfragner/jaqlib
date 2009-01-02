package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;
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
  public boolean doEvaluate(ResultType actual)
  {
    return !CompareUtil.areEqual(actual, expected);
  }

}
