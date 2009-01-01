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


  public void testToString_Null()
  {
    assertEquals("", CollectionUtil.toString(null, ""));
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