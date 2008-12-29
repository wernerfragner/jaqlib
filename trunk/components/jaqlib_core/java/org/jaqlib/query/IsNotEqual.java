package org.jaqlib.query;

import org.jaqlib.util.CompareUtil;
import org.jaqlib.util.reflect.MethodInvocation;

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
