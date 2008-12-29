package org.jaqlib.reflect;

import java.util.Collections;
import java.util.List;

/**
 * @author Werner Fragner
 */
public class NullMethodCallRecorder implements MethodCallRecorder
{

  public List<MethodInvocation> getAllInvocations()
  {
    return Collections.EMPTY_LIST;
  }


  public MethodInvocation getLastInvocation()
  {
    return null;
  }

}
