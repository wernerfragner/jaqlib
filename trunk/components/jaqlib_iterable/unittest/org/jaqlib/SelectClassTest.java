package org.jaqlib;

import java.util.List;


/**
 * @author Werner Fragner
 */
public class SelectClassTest extends AbstractSelectTest<SimpleTestElementImpl>
{


  @Override
  protected Class<SimpleTestElementImpl> getResultElementClass()
  {
    return SimpleTestElementImpl.class;
  }


  public void testCreateResultElement_Class_NoDefaultConstructor()
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


  public void testCreateResultElement_Class()
  {
    List<SimpleTestElementImpl> elements = createIsMatchElements();

    SimpleTestElementImpl testInterface = QB
        .getMethodCallRecorder(SimpleTestElementImpl.class);
    List<SimpleTestElementImpl> results = QB
        .select(SimpleTestElementImpl.class).from(elements).where(
            testInterface.isMatch()).isEqual(true).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }

}
