package org.jaqlib.util;

import java.util.Collection;


/**
 * Helper class for assertions.
 * 
 * @author Werner Fragner
 */
public class Assert
{

  public static <T> T notNull(T object)
  {
    return notNull(object, "Argument must not be null.");
  }


  public static <T> T notNull(T object, String message)
  {
    if (object == null)
    {
      throw new IllegalArgumentException(message);
    }
    return object;
  }


  public static <T> void notEmpty(Collection<T> elements, String message)
  {
    notNull(elements);
    if (elements.isEmpty())
    {
      throw new IllegalStateException(message);
    }
  }


  public static <T> void size(int size, T[] elements, String message)
  {
    notNull(elements);
    if (elements.length != size)
    {
      throw new IllegalStateException(message);
    }
  }


  public static <T> void size(int size, Collection<T> elements, String message)
  {
    notNull(elements);
    if (elements.size() != size)
    {
      throw new IllegalStateException(message);
    }
  }


  public static void state(boolean state, String message)
  {
    if (!state)
    {
      throw new IllegalStateException(message);
    }
  }


}
