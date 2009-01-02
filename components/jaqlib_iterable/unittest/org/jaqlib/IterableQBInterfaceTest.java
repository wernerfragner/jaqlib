package org.jaqlib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.jaqlib.core.reflect.RecordingInvocationHandler;

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


  public void testCreateResultElement_Interface()
  {
    Account testInterface = IterableQB.getRecorder(getAccountClass());
    assertNotNull(testInterface);

    InvocationHandler iHandler = Proxy.getInvocationHandler(testInterface);
    assertSame(RecordingInvocationHandler.class, iHandler.getClass());
  }


  public void testCreateResultElement_Class_NoCgLib()
  {
    try
    {
      IterableQB.getRecorder(AccountImpl.class);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException iae)
    {
    }
  }

}
