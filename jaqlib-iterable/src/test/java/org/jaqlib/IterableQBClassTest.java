package org.jaqlib;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for proxying classes with CGLib.
 * 
 * @author Werner Fragner
 */
public class IterableQBClassTest extends AbstractIterableTest<AccountImpl>
{


  @Override
  protected Class<AccountImpl> getAccountClass()
  {
    return AccountImpl.class;
  }

  @Test
  public void testCreateResultElement_Class_NoDefaultConstructor()
  {
    try
    {
      IterableQB.getRecorder(NoDefaultConstructurClass.class);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException iae)
    {
    }
  }

  @Test
  public void testCreateResultElement_Class()
  {
    List<AccountImpl> elements = createTestAccounts();

    AccountImpl testInterface = IterableQB.getRecorder(AccountImpl.class);
    List<AccountImpl> results = IterableQB.select().from(elements).whereCall(
        testInterface.getLastName()).isEqual("maier").asList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }

  @Test
  public void testCreateResultElement_BooleanMethodCallResult_Class()
  {
    List<AccountImpl> elements = createTestAccounts();

    AccountImpl testInterface = IterableQB.getRecorder(AccountImpl.class);
    List<AccountImpl> results = IterableQB.select().from(elements)
        .whereCallIsTrue(testInterface.isActive()).asList();
    assertNotNull(results);
    assertEquals(4, results.size());
  }

}
