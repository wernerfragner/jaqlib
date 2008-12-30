package org.jaqlib.util;

import junit.framework.TestCase;


public class ReflectionUtilTest extends TestCase
{

  public void testGetPlainClassNameObject_Null()
  {
    try
    {
      ReflectionUtil.getPlainClassName((Object) null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testGetPlainClassNameClass_Null()
  {
    try
    {
      ReflectionUtil.getPlainClassName((Class<?>) null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testGetPlainClassNameString_Null()
  {
    try
    {
      ReflectionUtil.getPlainClassName((String) null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testGetPlainClassNameString()
  {
    assertEquals("ReflectionUtilTest", ReflectionUtil.getPlainClassName(this));
  }

}
