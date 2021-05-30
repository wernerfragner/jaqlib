package org.jaqlib.core.reflect;

import java.util.List;

/**
 * @author Werner Fragner
 */
public class ThreadLocalMethodCallRecorder implements MethodCallRecorder
{

  private final ThreadLocal<MethodCallRecorder> methodCallRecorder = new ThreadLocal<>();


  public ThreadLocalMethodCallRecorder()
  {
    set(new NullMethodCallRecorder());
  }


  public void set(MethodCallRecorder recorder)
  {
    methodCallRecorder.set(recorder);
  }


  public List<MethodInvocation> getAllCurrentInvocations()
  {
    return getRecorder().getAllCurrentInvocations();
  }


  public MethodInvocation getCurrentInvocation()
  {
    return getRecorder().getCurrentInvocation();
  }


  public String getCurrentInvocationString()
  {
    return getRecorder().getCurrentInvocationString();
  }


  private MethodCallRecorder getRecorder()
  {
    return methodCallRecorder.get();
  }

}
