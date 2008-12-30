package org.jaqlib.util.reflect;

import java.util.Collections;
import java.util.List;

/**
 * @author Werner Fragner
 */
public class NullMethodCallRecorder implements MethodCallRecorder
{

  @SuppressWarnings("unchecked")
  public List<MethodInvocation> getAllCurrentInvocations()
  {
    return Collections.EMPTY_LIST;
  }


  public MethodInvocation getCurrentInvocation()
  {
    return null;
  }

}
