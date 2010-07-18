package org.jaqlib.util;

public class StringUtil
{

  public static boolean isEmpty(String str)
  {
    return (str == null || str.trim().equals(""));
  }

}
