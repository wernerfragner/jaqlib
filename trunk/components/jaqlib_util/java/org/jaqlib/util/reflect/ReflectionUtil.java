package org.jaqlib.util.reflect;

import java.util.HashSet;
import java.util.Set;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public class ReflectionUtil
{

  private static final String CGLIB_ENHANCER_CLASS = "net.sf.cglib.proxy.Enhancer";


  private static Class<?> getCgLibEnhancerClass()
  {
    try
    {
      return Class.forName(CGLIB_ENHANCER_CLASS);
    }
    catch (ClassNotFoundException e)
    {
      return null;
    }
    catch (NoClassDefFoundError e)
    {
      return null;
    }
  }


  public static boolean isCgLibAvailable()
  {
    return (getCgLibEnhancerClass() != null);
  }


  public static Class<?>[] getAllInterfaces(Class<?> clazz)
  {
    Set<Class<?>> interfaceSet = new HashSet<Class<?>>();
    addAllInterfaces(clazz, interfaceSet);
    return interfaceSet.toArray(new Class[interfaceSet.size()]);
  }


  private static void addAllInterfaces(Class<?> clazz,
      Set<Class<?>> interfaceSet)
  {
    Class<?>[] interfaces = clazz.getInterfaces();
    Class<?> superClass = clazz.getSuperclass();

    if (superClass != null && !superClass.equals(Object.class))
    {
      addAllInterfaces(superClass, interfaceSet);
    }

    for (Class<?> iface : interfaces)
    {
      interfaceSet.add(iface);
      addAllInterfaces(iface, interfaceSet);
    }
  }


  public static String getPlainClassName(Object obj)
  {
    Assert.notNull(obj);
    return getPlainClassName(obj.getClass());
  }


  public static String getPlainClassName(Class<?> clazz)
  {
    Assert.notNull(clazz);
    return getPlainClassName(clazz.getName());
  }


  public static String getPlainClassName(String className)
  {
    Assert.notNull(className);
    final int lastDotIdx = className.lastIndexOf('.');
    if (lastDotIdx > -1)
    {
      return className.substring(lastDotIdx + 1);
    }
    return className;
  }


}
