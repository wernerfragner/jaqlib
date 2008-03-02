package org.jaqlib.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jaqlib.util.Assert;


public class JaqlibInvocationHandler implements JaqlibInvocationRecorder,
    java.lang.reflect.InvocationHandler
{

  private final List<MethodInvocation> methodInvocations = new ArrayList<MethodInvocation>();


  public Object invoke(Object target, Method method, Object[] methodArgs)
      throws Throwable
  {
    methodInvocations.add(new MethodInvocation(method, methodArgs));
    return createReturnValue(method.getReturnType());
  }


  private Object createReturnValue(Class<?> returnType)
  {
    return StandardValueObjectFactory.newInstance(returnType);
  }


  public MethodInvocation getLastInvocation()
  {
    if (methodInvocations.isEmpty())
    {
      return null;
    }

    Assert.notEmpty(methodInvocations,
        "Cannot get last method invocation because no invocation "
            + "has been registered yet.");

    return methodInvocations.get(methodInvocations.size() - 1);
  }


  public List<MethodInvocation> getAllInvocations()
  {
    return new ArrayList<MethodInvocation>(methodInvocations);
  }

}
