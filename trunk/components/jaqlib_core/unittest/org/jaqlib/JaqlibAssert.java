package org.jaqlib;

import java.util.List;

import junit.framework.TestCase;

public class JaqlibAssert
{

  public static void assertEqualLists(List<?> list1, List<?> list2)
  {
    TestCase.assertEquals(list1.size(), list2.size());
    for (Object obj : list1)
    {
      TestCase.assertTrue(list2.contains(obj));
    }
  }

}
