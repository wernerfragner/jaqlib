package org.jaqlib.util.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jaqlib.util.Assert;

/**
 * This class records all method invocations on a {@link Proxy} object. <b>The
 * invocations are not delegated to the target. So the invocations always return
 * <tt>null</code>.</b>
 * 
 * @author Werner Fragner
 */
public class RecordingInvocationHandler implements MethodCallRecorder,
    java.lang.reflect.InvocationHandler
{

  private final LinkedList<MethodInvocation> methodInvocations = new LinkedList<MethodInvocation>();


  /**
   * {@inheritDoc}
   */
  public Object invoke(Object target, Method method, Object[] methodArgs)
      throws Throwable
  {
    methodInvocations.add(new MethodInvocation(method, methodArgs));
    return null;
  }


  /**
   * {@inheritDoc}
   */
  public MethodInvocation getCurrentInvocation()
  {
    if (methodInvocations.isEmpty())
    {
      return null;
    }

    Assert.notEmpty(methodInvocations,
        "Cannot get last method invocation because no invocation "
            + "has been recorded yet.");

    return methodInvocations.removeFirst();
  }


  /**
   * {@inheritDoc}
   */
  public List<MethodInvocation> getAllCurrentInvocations()
  {
    return new ArrayList<MethodInvocation>(methodInvocations);
  }

}
