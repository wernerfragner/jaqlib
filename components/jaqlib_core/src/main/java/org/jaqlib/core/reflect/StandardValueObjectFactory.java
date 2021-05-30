package org.jaqlib.core.reflect;


/**
 * @author Werner Fragner
 */
public class StandardValueObjectFactory
{

  /**
   * @param clazz the class to instantiate.
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
      return 0;
    }
    else if (clazz == Long.class || clazz == Long.TYPE)
    {
      return 0L;
    }
    else if (clazz == Short.class || clazz == Short.TYPE)
    {
      return (short) 0;
    }
    else if (clazz == Double.class || clazz == Double.TYPE)
    {
      return 0.0d;
    }
    else if (clazz == Float.class || clazz == Float.TYPE)
    {
      return 0.0f;
    }
    else if (clazz == Byte.class || clazz == Byte.TYPE)
    {
      return (byte) 0;
    }
    else if (clazz == Character.class || clazz == Character.TYPE)
    {
      return '\u0000';
    }
    else
    {
      return null;
    }
  }

}
