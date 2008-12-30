package org.jaqlib.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.jaqlib.util.Assert;
import org.jaqlib.util.ExceptionUtil;

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


  public static Class<?> getFieldType(Class<?> clz, String fieldName)
  {
    try
    {
      return clz.getDeclaredField(fieldName).getType();
    }
    catch (NoSuchFieldException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  public static void setFieldValue(Object target, String fieldName,
      Object fieldValue)
  {
    Assert.notNull(target);
    Assert.notNull(fieldName);

    try
    {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, fieldValue);
    }
    catch (NoSuchFieldException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
    catch (IllegalAccessException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  public static <T> T newInstance(Class<T> cls, Class<?>[] argTypes,
      Object[] args)
  {
    try
    {
      Constructor<T> c = cls.getConstructor(argTypes);
      return c.newInstance(args);
    }
    catch (NoSuchMethodException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
    catch (InstantiationException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
    catch (IllegalAccessException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
    catch (InvocationTargetException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  public static <T> T newInstance(Class<T> cls)
  {
    try
    {
      return cls.newInstance();
    }
    catch (InstantiationException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
    catch (IllegalAccessException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  public static boolean hasConstructor(Class<?> cls, Class<?>[] argTypes)
  {
    try
    {
      cls.getConstructor(argTypes);
      return true;
    }
    catch (NoSuchMethodException e)
    {
      return false;
    }
  }


}
