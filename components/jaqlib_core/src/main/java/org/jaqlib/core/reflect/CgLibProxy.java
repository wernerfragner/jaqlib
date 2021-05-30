package org.jaqlib.core.reflect;

import java.lang.reflect.InvocationHandler;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.transform.impl.UndeclaredThrowableStrategy;

import org.jaqlib.util.Assert;
import org.jaqlib.util.ReflectionUtil;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class CgLibProxy<T>
{

  private final ClassLoader classLoader;
  private final InvocationHandler invocationHandler;


  public CgLibProxy(ClassLoader classLoader, InvocationHandler invocationHandler)
  {
    this.classLoader = Assert.notNull(classLoader);
    this.invocationHandler = Assert.notNull(invocationHandler);
  }


  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> targetClass)
  {
    Enhancer enhancer = new Enhancer();
    enhancer.setClassLoader(classLoader);
    enhancer.setStrategy(new UndeclaredThrowableStrategy(
        java.lang.reflect.UndeclaredThrowableException.class));
    enhancer.setSuperclass(targetClass);
    enhancer.setInterfaces(getInterfaces(targetClass));
    enhancer.setInterceptDuringConstruction(false);

    Callback[] callbacks = new Callback[] { new CgLibInvocationHandlerAdapter(
        invocationHandler) };
    enhancer.setCallbacks(callbacks);

    Class<?>[] types = new Class[] { net.sf.cglib.proxy.InvocationHandler.class };
    enhancer.setCallbackTypes(types);

    return (T) enhancer.create();
  }


  private Class<?>[] getInterfaces(Class<T> targetClass)
  {
    return ReflectionUtil.getAllInterfaces(targetClass);
  }


}
