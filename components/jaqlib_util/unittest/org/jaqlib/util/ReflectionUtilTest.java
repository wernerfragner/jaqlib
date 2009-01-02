package org.jaqlib.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;


public class ReflectionUtilTest extends TestCase
{


  public void testGetFieldType_Null()
  {
    try
    {
      ReflectionUtil.getFieldType(null, "id");
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
    try
    {
      ReflectionUtil.getFieldType(SomeClass.class, null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testGetFieldType()
  {
    assertEquals(Long.class, ReflectionUtil.getFieldType(SomeClass.class, "id"));
  }


  public void testGetFieldType_FieldNameNotExisting()
  {
    try
    {
      ReflectionUtil.getFieldType(SomeClass.class, "nonExisting");
      fail("Did not throw RuntimeException");
    }
    catch (RuntimeException e)
    {
    }
  }


  public void testSetFieldValue_Null()
  {
    setFieldValue_Null(null, "id", Long.valueOf(1));
    setFieldValue_Null(new SomeClass(), null, Long.valueOf(1));
  }


  private void setFieldValue_Null(Object object, String fieldName, Object value)
  {
    try
    {
      ReflectionUtil.setFieldValue(object, fieldName, value);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testSetFieldValue()
  {
    SomeClass obj = new SomeClass();
    Long fieldValue = Long.valueOf(1);

    assertNull(obj.getId());
    ReflectionUtil.setFieldValue(obj, "id", fieldValue);
    assertSame(fieldValue, obj.getId());
  }


  public void testSetFieldValue_SetToNull()
  {
    // set value
    SomeClass obj = new SomeClass();
    Long fieldValue = Long.valueOf(1);
    ReflectionUtil.setFieldValue(obj, "id", fieldValue);
    assertNotNull(obj.getId());

    // set null
    ReflectionUtil.setFieldValue(obj, "id", null);
    assertNull(obj.getId());
  }


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


  public void testGetPlainClassNameObject()
  {
    assertEquals("ReflectionUtilTest", ReflectionUtil.getPlainClassName(this));
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


  public void testGetPlainClassNameClass()
  {
    assertEquals("ReflectionUtilTest", ReflectionUtil
        .getPlainClassName(getClass()));
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
    assertEquals("SomeClass", ReflectionUtil.getPlainClassName("SomeClass"));
  }


  /**
   * Given class does not implement any interfaces.
   */
  public void testGetAllInterfaces_NoInterfaces()
  {
    assertEquals(0, ReflectionUtil.getAllInterfaces(Object.class).length);
  }


  /**
   * Given class is itself an interface.
   */
  public void testGetAllInterfaces_Interface()
  {
    assertEquals(1, ReflectionUtil
        .getAllInterfaces(SomeAbstractInterface.class).length);
  }


  /**
   * All interfaces of the entire inheritence tree must be returned.
   */
  public void testGetAllInterfaces()
  {
    Class<?>[] interfaces = ReflectionUtil.getAllInterfaces(SomeClass.class);
    assertEquals(3, interfaces.length);
    List<Class<?>> interfaceList = Arrays.asList(interfaces);
    assertTrue(interfaceList.contains(SomeInterface.class));
    assertTrue(interfaceList.contains(SomeBaseInterface.class));
    assertTrue(interfaceList.contains(SomeAbstractInterface.class));
  }


  public void testHasDeclaredMethod()
  {
    assertFalse(ReflectionUtil.hasDeclaredMethod(getClass(), "toString"));
    assertTrue(ReflectionUtil.hasDeclaredMethod(getClass(),
        "testHasDeclaredMethod"));
  }

}
