package org.jaqlib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.jaqlib.util.reflect.RecordingInvocationHandler;

/**
 * Test class for proxying interfaces with JDK dynamic proxy mechanism.
 * 
 * @author Werner Fragner
 */
public class IterableInterfaceTest extends AbstractIterableTest<Account>
{

  @Override
  protected Class<Account> getAccountClass()
  {
    return Account.class;
  }


  public void testCreateResultElement_Interface()
  {
    Account testInterface = QB.getMethodCallRecorder(getAccountClass());
    assertNotNull(testInterface);

    InvocationHandler iHandler = Proxy.getInvocationHandler(testInterface);
    assertSame(RecordingInvocationHandler.class, iHandler.getClass());
  }


  public void testCreateResultElement_Class_NoCgLib()
  {
    try
    {
      QB.getMethodCallRecorder(AccountImpl.class);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException iae)
    {
    }
  }

}
