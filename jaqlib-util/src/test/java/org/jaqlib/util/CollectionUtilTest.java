package org.jaqlib.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionUtilTest
{

  private static final String STR = "str";

  private List<Object> list;


  @BeforeEach
  void setUp()
  {
    list = CollectionUtil.newDefaultList();
    list.add(new Dummy());
    list.add(new Dummy());
    list.add(new Dummy());
  }


  @Test
  void testToString_ObjectArray_Null()
  {
    Object[] arr = new Object[] { new Dummy(), new Dummy(), new Dummy() };

    assertEquals("", CollectionUtil.toString((Object[]) null, ""));
    assertEquals(STR + STR + STR, CollectionUtil.toString(arr, null));
  }

  @Test
  void testToString_Iterable_Null()
  {
    assertEquals("", CollectionUtil.toString((Iterable<?>) null, ""));
    assertEquals(STR + STR + STR, CollectionUtil.toString(list, null));
  }

  @Test
  void testToString_MultipleValues()
  {
    assertEquals(STR + "," + STR + "," + STR, CollectionUtil
        .toString(list, ","));
  }

  @Test
  void testToString_SingleValue()
  {
    list = CollectionUtil.newDefaultList();
    list.add(new Dummy());
    assertEquals(STR, CollectionUtil.toString(list, ","));
  }

  @Test
  void testToString_NoValue()
  {
    list = CollectionUtil.newDefaultList();
    assertEquals("", CollectionUtil.toString(list, ","));
  }


  private static class Dummy
  {

    @Override
    public String toString()
    {
      return STR;
    }

  }

}
