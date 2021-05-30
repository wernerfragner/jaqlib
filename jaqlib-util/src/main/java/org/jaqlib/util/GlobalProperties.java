package org.jaqlib.util;

public class GlobalProperties
{
  private static final String PROP_DISABLE_CGLIB = "jaqlib.disable-cglib";

  public static void setCgLibDisabled(boolean val)
  {
    System.setProperty(PROP_DISABLE_CGLIB, Boolean.toString(val));
  }

  public static boolean isCgLibDisabled()
  {
    String val = System.getProperty(PROP_DISABLE_CGLIB);
    return Boolean.TRUE.toString().equalsIgnoreCase(val);
  }

}
