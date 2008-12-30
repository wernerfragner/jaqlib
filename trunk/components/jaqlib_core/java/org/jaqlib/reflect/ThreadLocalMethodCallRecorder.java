package org.jaqlib.reflect;

import java.util.List;

/**
 * @author Werner Fragner
 */
public class ThreadLocalMethodCallRecorder implements MethodCallRecorder
{

  private final ThreadLocal<MethodCallRecorder> methodCallRecorder = new ThreadLocal<MethodCallRecorder>();


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


  private MethodCallRecorder getRecorder()
  {
    return methodCallRecorder.get();
  }


}
