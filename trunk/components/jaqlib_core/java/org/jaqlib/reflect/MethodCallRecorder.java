package org.jaqlib.reflect;

import java.util.List;


/**
 * @author Werner Fragner
 */
public interface MethodCallRecorder
{

  MethodInvocation getLastInvocation();


  List<MethodInvocation> getAllInvocations();

}
