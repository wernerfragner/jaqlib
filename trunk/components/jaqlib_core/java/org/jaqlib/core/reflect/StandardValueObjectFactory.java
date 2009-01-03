package org.jaqlib.core.reflect;


/**
 * @author Werner Fragner
 */
public class StandardValueObjectFactory
{

  /**
   * @param clazz
   * @return the default instance for the given (primitive) type.
   */
  public static Object newInstance(Class<?> clazz)
  {
    if (clazz == Boolean.class || clazz == Boolean.TYPE)
    {
      return Boolean.FALSE;
    }
    else if (clazz == Integer.class || clazz == Integer.TYPE)
    {
      return Integer.valueOf(0);
    }
    else if (clazz == Long.class || clazz == Long.TYPE)
    {
      return Long.valueOf(0);
    }
    else if (clazz == Short.class || clazz == Short.TYPE)
    {
      return Short.valueOf((short) 0);
    }
    else if (clazz == Double.class || clazz == Double.TYPE)
    {
      return Double.valueOf(0.0d);
    }
    else if (clazz == Float.class || clazz == Float.TYPE)
    {
      return Float.valueOf(0.0f);
    }
    else if (clazz == Byte.class || clazz == Byte.TYPE)
    {
      return Byte.valueOf((byte) 0);
    }
    else if (clazz == Character.class || clazz == Character.TYPE)
    {
      return Character.valueOf('\u0000');
    }
    else
    {
      return null;
    }
  }

}
