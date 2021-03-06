package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;

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


  @Override
  public boolean doEvaluate(ResultType actual)
  {
    return actual != null;
  }


}
