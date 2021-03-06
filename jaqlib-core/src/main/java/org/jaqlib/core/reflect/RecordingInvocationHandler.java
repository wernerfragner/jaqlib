package org.jaqlib.core.reflect;

import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;
import org.jaqlib.util.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class records all method invocations on a
 * {@link java.lang.reflect.Proxy} object. <b>The invocations are not delegated
 * to the target. So the invocations always return <tt>null</code>.</b>
 * 
 * @author Werner Fragner
 */
public class RecordingInvocationHandler implements MethodCallRecorder,
    java.lang.reflect.InvocationHandler
{

  private final Logger log = LogUtil.getLogger(this);

  private final LinkedList<MethodInvocation> methodInvocations = new LinkedList<>();


  /**
   * {@inheritDoc}
   */
  public Object invoke(Object target, Method method, Object[] methodArgs)
  {
    if (shouldRecordMethodCall(target, method))
    {
      if (log.isLoggable(Level.FINE))
      {
        String s = "Recording method call. Method: " + method
            + "; MethodArguments: " + CollectionUtil.toString(methodArgs, ",");
        log.fine(s);
      }

      methodInvocations.add(new MethodInvocation(method, methodArgs));
    }

    // return default instance (needed for CGLib when unboxing types)
    return StandardValueObjectFactory.newInstance(method.getReturnType());
  }


  private boolean shouldRecordMethodCall(Object target, Method method)
  {
    // ignore Object.finalize() method
    return !method.getDeclaringClass().equals(Object.class)
            || !method.getName().equals("finalize");
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
        "Cannot get current method invocation because no invocation "
            + "has been recorded yet.");

    return methodInvocations.removeFirst();
  }


  /**
   * {@inheritDoc}
   */
  public List<MethodInvocation> getAllCurrentInvocations()
  {
    return new ArrayList<>(methodInvocations);
  }


  public String getCurrentInvocationString()
  {
    if (methodInvocations.isEmpty())
    {
      return "";
    }
    return methodInvocations.getFirst().toString();
  }

}
