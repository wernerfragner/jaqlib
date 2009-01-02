package org.jaqlib.core.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Werner Fragner
 */
public class MethodInvocation
{

  private final Method method;
  private final Object[] methodArgs;


  public MethodInvocation(Method method, Object[] methodArgs)
  {
    this.method = method;
    this.methodArgs = methodArgs;
  }


  public Object invoke(Object target)
  {
    try
    {
      return method.invoke(target, methodArgs);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
    catch (InvocationTargetException e)
    {
      throw new RuntimeException(e);
    }
  }


}
