package org.jaqlib.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jaqlib.util.lang.SaveConversions;


/**
 * @author Werner Fragner
 */
public class ReflectionUtil
{

  private static final String CGLIB_ENHANCER_CLASS = "net.sf.cglib.proxy.Enhancer";


  public static boolean isPrimitiveType(Class<?> clz)
  {
    if (clz.isPrimitive() || clz.isEnum())
    {
      return true;
    }
    return clz.equals(String.class) || clz.equals(Short.class)
        || clz.equals(Integer.class) || clz.equals(Long.class)
        || clz.equals(Float.class) || clz.equals(Double.class)
        || clz.equals(Byte.class) || clz.equals(Character.class)
        || clz.equals(Boolean.class);
  }


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
    if (clazz.isInterface())
    {
      interfaceSet.add(clazz);
    }

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


  /**
   * @param clz
   * @param fieldName
   * @return the given field type of the given class. The entire inheritance
   *         tree of the given class is searched for the given field.
   * 
   * @throws RuntimeException if the given field was not found.
   */
  public static Class<?> getFieldType(Class<?> clz, String fieldName)
  {
    return getField(clz, fieldName).getType();
  }


  /**
   * @param clz
   * @param fieldName
   * @return the given field of the given class. The entire inheritance tree of
   *         the given class is searched for the given field.
   * 
   * @throws RuntimeException if the given field was not found.
   */
  public static Field getField(Class<?> clz, String fieldName)
  {
    Assert.notNull(clz);
    Assert.notNull(fieldName);

    try
    {
      return clz.getDeclaredField(fieldName);
    }
    catch (NoSuchFieldException e)
    {
      if (clz.getSuperclass() == null)
      {
        throw ExceptionUtil.toRuntimeException(e);
      }
      else
      {
        return getField(clz.getSuperclass(), fieldName);
      }
    }
  }


  public static void setFieldValue(Object target, String fieldName,
      Object fieldValue)
  {
    Assert.notNull(target);
    Assert.notNull(fieldName);

    if (!setFieldValueWithSetter(target, fieldName, fieldValue))
    {
      setFieldValueDirect(target, fieldName, fieldValue);
    }
  }


  private static boolean setFieldValueWithSetter(Object target,
      String fieldName, Object fieldValue)
  {
    try
    {
      PropertyDescriptor desc = new PropertyDescriptor(fieldName,
          target.getClass());
      Method m = desc.getWriteMethod();
      m.invoke(target, fieldValue);
      return true;
    }
    catch (IllegalArgumentException e)
    {
    }
    catch (IntrospectionException e)
    {
    }
    catch (IllegalAccessException e)
    {
    }
    catch (InvocationTargetException e)
    {
    }

    return false;
  }


  private static void setFieldValueDirect(Object target, String fieldName,
      Object fieldValue)
  {
    Field field = getField(target.getClass(), fieldName);
    field.setAccessible(true);

    try
    {
      field.set(target, fieldValue);
    }
    catch (IllegalArgumentException e)
    {
      final Object convertedFieldValue = saveConvert(fieldValue,
          field.getType());

      if (convertedFieldValue == fieldValue)
      {
        // no conversion performed
        throw ExceptionUtil.toRuntimeException(e);
      }
      else
      {
        // conversion performed, try to set field again with converted value
        setFieldValue(target, fieldName, convertedFieldValue);
      }
    }
    catch (IllegalAccessException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  private static Object saveConvert(Object value, Class<?> targetType)
  {
    return SaveConversions.convert(value, targetType);
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


  /**
   * @param cls
   * @param methodName
   * @return true if the given class declares the given method. This method does
   *         not search the inheritance tree for the given method. So if a super
   *         class of the given class declares the given method then false is
   *         returned.
   */
  public static boolean hasDeclaredMethod(Class<?> cls, String methodName)
  {
    try
    {
      cls.getDeclaredMethod(methodName, new Class[0]);
      return true;
    }
    catch (NoSuchMethodException e)
    {
      return false;
    }
  }


  public static Object getFieldValue(Object element, String fieldName)
  {
    try
    {
      Field field = getField(element.getClass(), fieldName);
      field.setAccessible(true);
      return field.get(element);
    }
    catch (IllegalAccessException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  public static Class<?> getCollectionElementClass(Field field)
  {
    if (isCollection(field.getType()))
    {
      Type type = field.getGenericType();
      if (type instanceof ParameterizedType)
      {
        ParameterizedType pt = (ParameterizedType) type;
        Type[] elementTypes = pt.getActualTypeArguments();

        if (elementTypes.length == 1)
        {
          return getClass(elementTypes[0]);
        }
        else
        {
          throw new IllegalArgumentException(
              "The collection of the given field '" + field
                  + "' has multiple element types. Only one is supported.");
        }
      }
      else
      {
        throw new IllegalArgumentException(
            "The collection of the given field '" + field + "' is not generic.");
      }
    }
    else
    {
      throw new IllegalArgumentException("Given field '" + field
          + "' is no collection.");
    }
  }


  private static Class<?> getClass(Type type)
  {
    if (type instanceof Class)
    {
      return (Class<?>) type;
    }
    return type.getClass();
  }


  public static boolean isCollection(Class<?> clz)
  {
    return Collection.class.isAssignableFrom(clz);
  }


  public static boolean isGeneric(Field field)
  {
    Type type = field.getGenericType();
    if (type instanceof ParameterizedType)
    {
      ParameterizedType pt = (ParameterizedType) type;
      return (pt.getActualTypeArguments().length > 0);
    }
    return false;
  }


  public static boolean isAbstract(Class<?> fieldType)
  {
    return fieldType.isInterface()
        || Modifier.isAbstract(fieldType.getModifiers());
  }

}
