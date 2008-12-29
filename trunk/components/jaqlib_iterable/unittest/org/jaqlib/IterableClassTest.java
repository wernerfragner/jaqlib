package org.jaqlib;

import java.util.List;


/**
 * Test class for proxying classes with CGLib.
 * 
 * @author Werner Fragner
 */
public class IterableClassTest extends AbstractIterableTest<AccountImpl>
{


  @Override
  protected Class<AccountImpl> getAccountClass()
  {
    return AccountImpl.class;
  }


  public void testCreateResultElement_Class_NoDefaultConstructor()
  {
    try
    {
      IterableQB.getMethodCallRecorder(NoDefaultConstructurClass.class);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException iae)
    {
    }
  }


  public void testCreateResultElement_Class()
  {
    List<AccountImpl> elements = createTestAccounts();

    AccountImpl testInterface = IterableQB.getMethodCallRecorder(AccountImpl.class);
    List<AccountImpl> results = IterableQB.select(AccountImpl.class).from(elements)
        .where(testInterface.getLastName()).isEqual("maier").asList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }

}
