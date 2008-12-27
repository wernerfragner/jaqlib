package org.jaqlib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.jaqlib.reflect.RecordingInvocationHandler;

/**
 * @author Werner Fragner
 */
public class SelectInterfaceTest extends AbstractSelectTest<SimpleTestElement>
{

  @Override
  protected Class<SimpleTestElement> getResultElementClass()
  {
    return SimpleTestElement.class;
  }


  public void testCreateResultElement_Interface()
  {
    SimpleTestElement testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    assertNotNull(testInterface);

    InvocationHandler iHandler = Proxy.getInvocationHandler(testInterface);
    assertSame(RecordingInvocationHandler.class, iHandler.getClass());
  }


  public void testCreateResultElement_Class_NoCgLib()
  {
    try
    {
      QB.getMethodCallRecorder(SimpleTestElementImpl.class);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException iae)
    {
    }
  }

}
