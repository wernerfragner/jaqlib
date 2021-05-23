package org.jaqlib.util;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ReflectionUtilTest
{

  @Test
  void testGetFieldType_Null()
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

  @Test
  void testGetFieldType()
  {
    assertEquals(Long.class, ReflectionUtil.getFieldType(SomeClass.class, "id"));
  }

  @Test
  void testGetFieldType_FieldNameNotExisting()
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

  @Test
  void testSetFieldValue_Null()
  {
    setFieldValue_Null(null, "id", 1L);
    setFieldValue_Null(new SomeClass(), null, 1L);
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

  @Test
  void testSetFieldValue()
  {
    SomeClass obj = new SomeClass();
    Long fieldValue = 1L;

    assertNull(obj.getId());
    ReflectionUtil.setFieldValue(obj, "id", fieldValue);
    assertSame(fieldValue, obj.getId());
  }

  @Test
  void testSetFieldValue_SetToNull()
  {
    // set value
    SomeClass obj = new SomeClass();
    Long fieldValue = 1L;
    ReflectionUtil.setFieldValue(obj, "id", fieldValue);
    assertNotNull(obj.getId());

    // set null
    ReflectionUtil.setFieldValue(obj, "id", null);
    assertNull(obj.getId());
  }

  @Test
  void testGetPlainClassNameObject_Null()
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

  @Test
  void testGetPlainClassNameObject()
  {
    assertEquals("ReflectionUtilTest", ReflectionUtil.getPlainClassName(this));
  }

  @Test
  void testGetPlainClassNameClass_Null()
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

  @Test
  void testGetPlainClassNameClass()
  {
    assertEquals("ReflectionUtilTest", ReflectionUtil
        .getPlainClassName(getClass()));
  }

  @Test
  void testGetPlainClassNameString_Null()
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

  @Test
  void testGetPlainClassNameString()
  {
    assertEquals("SomeClass", ReflectionUtil.getPlainClassName("SomeClass"));
  }


  /**
   * Given class does not implement any interfaces.
   */
  @Test
  void testGetAllInterfaces_NoInterfaces()
  {
    assertEquals(0, ReflectionUtil.getAllInterfaces(Object.class).length);
  }


  /**
   * Given class is itself an interface.
   */
  @Test
  void testGetAllInterfaces_Interface()
  {
    assertEquals(1, ReflectionUtil
        .getAllInterfaces(SomeAbstractInterface.class).length);
  }


  /**
   * All interfaces of the entire inheritence tree must be returned.
   */
  @Test
  void testGetAllInterfaces()
  {
    Class<?>[] interfaces = ReflectionUtil.getAllInterfaces(SomeClass.class);
    assertEquals(3, interfaces.length);
    List<Class<?>> interfaceList = Arrays.asList(interfaces);
    assertTrue(interfaceList.contains(SomeInterface.class));
    assertTrue(interfaceList.contains(SomeBaseInterface.class));
    assertTrue(interfaceList.contains(SomeAbstractInterface.class));
  }


  @Test
  void testHasDeclaredMethod()
  {
    assertFalse(ReflectionUtil.hasDeclaredMethod(getClass(), "toString"));
    assertTrue(ReflectionUtil.hasDeclaredMethod(getClass(),
        "testHasDeclaredMethod"));
  }

}
