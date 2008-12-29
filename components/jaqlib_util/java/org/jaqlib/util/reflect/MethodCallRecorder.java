package org.jaqlib.util.reflect;

import java.util.List;


/**
 * @author Werner Fragner
 */
public interface MethodCallRecorder
{

  /**
   * @return the current invocation. That means that if multiple invocations
   *         have been made that the first invocation is returned. Successive
   *         calls to this method return the next invocation (and so on).
   *         Returns null if no invocation has been made yet.
   */
  MethodInvocation getCurrentInvocation();


  /**
   * @return all previously executed method invocations. Returns an empty list
   *         if no invocations have been made yet.
   */
  List<MethodInvocation> getAllCurrentInvocations();

}
