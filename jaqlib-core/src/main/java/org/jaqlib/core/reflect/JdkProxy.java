package org.jaqlib.core.reflect;

import org.jaqlib.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class JdkProxy<T>
{

  private final ClassLoader classLoader;
  private final InvocationHandler invocationHandler;


  public JdkProxy(ClassLoader classLoader, InvocationHandler invocationHandler)
  {
    this.classLoader = Assert.notNull(classLoader);
    this.invocationHandler = Assert.notNull(invocationHandler);
  }


  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> targetClass)
  {
    final Class[] classes = new Class[] { targetClass};
    return (T) Proxy.newProxyInstance(classLoader, classes, invocationHandler);
  }

}
