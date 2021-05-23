package org.jaqlib.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Werner Fragner
 */
public class NoActionInvocationHandler implements InvocationHandler
{

  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable
  {
    return null;
  }

}
