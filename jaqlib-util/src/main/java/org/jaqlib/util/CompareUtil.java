package org.jaqlib.util;

/**
 * @author Werner Fragner
 */
public class CompareUtil
{

  public static boolean areEqual(Object obj1, Object obj2)
  {
    if (obj1 == null)
    {
      return obj2 == null;
    }
    if (obj2 == null)
    {
      return false;
    }
    return obj1.equals(obj2);
  }

}
