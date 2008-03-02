package org.jaqlib;

import java.util.List;


/**
 * @author Werner Fragner
 */
public class SelectClassTest extends AbstractSelectTest<SimpleTestItemImpl>
{


  @Override
  protected Class<SimpleTestItemImpl> getResultItemClass()
  {
    return SimpleTestItemImpl.class;
  }


  public void testCreateResultItem_Class_NoDefaultConstructor()
  {
    try
    {
      QB.getMethodCallRecorder(NoDefaultConstructurClass.class);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException iae)
    {
    }
  }


  public void testCreateResultItem_Class()
  {
    List<? extends SimpleTestItemImpl> items = createIsMatchItems();

    SimpleTestItemImpl testInterface = QB
        .getMethodCallRecorder(SimpleTestItemImpl.class);
    List<SimpleTestItemImpl> results = QB.select(SimpleTestItemImpl.class)
        .from(items).where(testInterface.isMatch()).isEqual(true).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(items.get(1), results.get(0));
  }

}
