package org.jaqlib;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JaqlibAssert
{

  public static void assertEqualLists(List<?> list1, List<?> list2)
  {
    assertEquals(list1.size(), list2.size());
    for (Object obj : list1)
    {
      assertTrue(list2.contains(obj));
    }
  }

}
