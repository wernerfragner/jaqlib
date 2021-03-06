package org.jaqlib.core.reflect;

import net.sf.cglib.proxy.InvocationHandler;
import org.jaqlib.util.Assert;

import java.lang.reflect.Method;

/**
 * @author Werner Fragner
 */
public class CgLibInvocationHandlerAdapter implements InvocationHandler
{

  private final java.lang.reflect.InvocationHandler jdkInvocationHandler;


  public CgLibInvocationHandlerAdapter(
      java.lang.reflect.InvocationHandler jdkInvocationHandler)
  {
    this.jdkInvocationHandler = Assert.notNull(jdkInvocationHandler);
  }


  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable
  {
    return jdkInvocationHandler.invoke(proxy, method, args);
  }

}
