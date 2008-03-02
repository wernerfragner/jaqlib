package org.jaqlib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.jaqlib.reflect.JaqlibInvocationHandler;

/**
 * @author Werner Fragner
 */
public class SelectInterfaceTest extends AbstractSelectTest<SimpleTestItem>
{

  @Override
  protected Class<SimpleTestItem> getResultItemClass()
  {
    return SimpleTestItem.class;
  }


  public void testCreateResultItem_Interface()
  {
    SimpleTestItem testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    assertNotNull(testInterface);

    InvocationHandler iHandler = Proxy.getInvocationHandler(testInterface);
    assertSame(JaqlibInvocationHandler.class, iHandler.getClass());
  }


  public void testCreateResultItem_Class_NoCgLib()
  {
    try
    {
      QB.getMethodCallRecorder(SimpleTestItemImpl.class);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException iae)
    {
    }
  }

}
