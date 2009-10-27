package org.jaqlib.core.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jaqlib.util.ExceptionUtil;
import org.jaqlib.util.ReflectionUtil;

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
      throw ExceptionUtil.toRuntimeException("Could not invoke method '"
          + toString() + "' on target object '" + target + "'", e);
    }
    catch (InvocationTargetException e)
    {
      throw ExceptionUtil.toRuntimeException("Could not invoke method '"
          + toString() + "' on target object '" + target + "'", e);
    }
  }


  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(method.getName());
    sb.append("(");
    if (methodArgs != null)
    {
      boolean first = true;
      for (Object methodArg : methodArgs)
      {
        if (first)
        {
          first = false;
        }
        else
        {
          sb.append(", ");
        }
        sb.append(ReflectionUtil.getPlainClassName(methodArg));
      }
    }
    sb.append(")");
    return sb.toString();
  }

}
