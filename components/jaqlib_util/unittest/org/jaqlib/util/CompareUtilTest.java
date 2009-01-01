package org.jaqlib.util;

import junit.framework.TestCase;

public class CompareUtilTest extends TestCase
{

  public void testAreEqual()
  {
    TestEqualClass obj1 = new TestEqualClass(1);
    TestEqualClass obj2 = new TestEqualClass(1);
    TestEqualClass obj3 = new TestEqualClass(2);

    assertFalse(CompareUtil.areEqual(obj1, obj3));
    assertFalse(CompareUtil.areEqual(obj1, null));
    assertFalse(CompareUtil.areEqual(null, obj2));
    assertTrue(CompareUtil.areEqual(null, null));
    assertTrue(CompareUtil.areEqual(obj1, obj2));
    assertTrue(CompareUtil.areEqual(obj2, obj1));
    assertTrue(CompareUtil.areEqual(obj1, obj1));
    assertTrue(CompareUtil.areEqual(obj2, obj2));
  }


  public static class TestEqualClass
  {

    private final Integer value;


    public TestEqualClass(Integer value)
    {
      this.value = value;
    }


    @Override
    public boolean equals(Object obj)
    {
      if (obj instanceof TestEqualClass)
      {
        return this.value.equals(((TestEqualClass) obj).value);
      }
      return false;
    }

  }


}
