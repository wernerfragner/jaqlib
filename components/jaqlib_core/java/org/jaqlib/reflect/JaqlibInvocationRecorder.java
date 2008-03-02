package org.jaqlib.reflect;

import java.util.List;


/**
 * @author Werner Fragner
 */
public interface JaqlibInvocationRecorder
{

  MethodInvocation getLastInvocation();


  List<MethodInvocation> getAllInvocations();

}
