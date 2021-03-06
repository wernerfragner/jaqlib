package org.jaqlib;

import org.jaqlib.core.reflect.RecordingInvocationHandler;
import org.jaqlib.util.GlobalProperties;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for proxying interfaces with JDK dynamic proxy mechanism.
 * 
 * @author Werner Fragner
 */
public class IterableQBInterfaceTest extends AbstractIterableTest<Account>
{

  @Override
  protected Class<Account> getAccountClass()
  {
    return Account.class;
  }

  @Test
  public void testCreateResultElement_Class_NoCgLib()
  {
    boolean isDisabled = GlobalProperties.isCgLibDisabled();
    GlobalProperties.setCgLibDisabled(true);

    try
    {
      IterableQB.getRecorder(AccountImpl.class);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException iae)
    {
    }
    finally
    {
      GlobalProperties.setCgLibDisabled(isDisabled);
    }
  }

  @Test
  public void testCreateResultElement_Interface()
  {
    Account testInterface = IterableQB.getRecorder(getAccountClass());
    assertNotNull(testInterface);

    InvocationHandler iHandler = Proxy.getInvocationHandler(testInterface);
    assertSame(RecordingInvocationHandler.class, iHandler.getClass());
  }

  @Test
  public void testCreateResultElement_BooleanMethodCallResult_Interface()
  {
    List<Account> elements = createTestAccounts();

    Account testInterface = IterableQB.getRecorder(Account.class);
    List<Account> results = IterableQB.select().from(elements).whereCallIsTrue(
        testInterface.isActive()).asList();
    assertNotNull(results);
    assertEquals(4, results.size());
  }

}
