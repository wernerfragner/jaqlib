package org.jaqlib.util;

import java.util.List;

import junit.framework.TestCase;

public class CollectionUtilTest extends TestCase
{

  private static final String STR = "str";

  private List<Object> list;


  @Override
  public void setUp()
  {
    list = CollectionUtil.newDefaultList();
    list.add(new Dummy());
    list.add(new Dummy());
    list.add(new Dummy());
  }


  public void testToString_ObjectArray_Null()
  {
    Object[] arr = new Object[] { new Dummy(), new Dummy(), new Dummy() };

    assertEquals("", CollectionUtil.toString((Object[]) null, ""));
    assertEquals(STR + STR + STR, CollectionUtil.toString(arr, null));
  }


  public void testToString_Iterable_Null()
  {
    assertEquals("", CollectionUtil.toString((Iterable<?>) null, ""));
    assertEquals(STR + STR + STR, CollectionUtil.toString(list, null));
  }


  public void testToString_MultipleValues()
  {
    assertEquals(STR + "," + STR + "," + STR, CollectionUtil
        .toString(list, ","));
  }


  public void testToString_SingleValue()
  {
    list = CollectionUtil.newDefaultList();
    list.add(new Dummy());
    assertEquals(STR, CollectionUtil.toString(list, ","));
  }


  public void testToString_NoValue()
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
