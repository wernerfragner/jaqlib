package org.jaqlib.reflect;

import org.jaqlib.util.Assert;
import org.jaqlib.util.reflect.CgLibProxy;
import org.jaqlib.util.reflect.JdkProxy;
import org.jaqlib.util.reflect.ReflectionUtil;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class JaqlibProxy<T>
{

  private final ClassLoader classLoader;
  private final JaqlibInvocationHandler invocationHandler = new JaqlibInvocationHandler();


  public JaqlibProxy(ClassLoader classLoader)
  {
    this.classLoader = Assert.notNull(classLoader);
  }


  public T getProxy(Class<T> targetClass)
  {
    Assert.notNull(targetClass, "Cannot proxy a null class.");

    if (isInterface(targetClass))
    {
      return getJdkProxy(targetClass);
    }
    else if (isCgLibAvailable())
    {
      return getCgLibProxy(targetClass);
    }

    throw new IllegalArgumentException("Cannot proxy class because CGLib is "
        + "not on classpath. Use an interface instead or put CGLib "
        + "on the classpath.");
  }


  private boolean isCgLibAvailable()
  {
    return ReflectionUtil.isCgLibAvailable();
  }


  private T getCgLibProxy(Class<T> targetClass)
  {
    assertHasDefaultConstructor(targetClass);

    CgLibProxy<T> proxy = new CgLibProxy<T>(classLoader, invocationHandler);
    return proxy.getProxy(targetClass);
  }


  private void assertHasDefaultConstructor(Class<T> targetClass)
  {
    try
    {
      targetClass.getConstructor();
    }
    catch (NoSuchMethodException e)
    {
      throw new IllegalArgumentException("Cannot proxy class "
          + targetClass.getName()
          + ". The class does not have a default constructor.");
    }
  }


  private boolean isInterface(Class<T> clazz)
  {
    return clazz.isInterface();
  }


  private T getJdkProxy(Class<T> targetClass)
  {
    JdkProxy<T> proxy = new JdkProxy<T>(classLoader, invocationHandler);
    return proxy.getProxy(targetClass);
  }


  public JaqlibInvocationRecorder getInvocationRecorder()
  {
    return invocationHandler;
  }

}
