package org.jaqlib.core.bean;

import junit.framework.TestCase;

import org.jaqlib.Account;
import org.jaqlib.CreditRating;
import org.jaqlib.CreditRatingTypeHandler;
import org.jaqlib.core.bean.DefaultJavaTypeHandlerRegistry;
import org.jaqlib.core.bean.NullJavaTypeHandler;

public class DefaultJavaTypeHandlerRegistryTest extends TestCase
{

  private DefaultJavaTypeHandlerRegistry registry;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    registry = new DefaultJavaTypeHandlerRegistry();
  }


  public void testGetTypeHandler_Null()
  {
    try
    {
      registry.getTypeHandler(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  /**
   * No java type handler for given class available. A NullJavaTypeHandler must
   * be returned.
   */
  public void testGetTypeHandler_NoneAvailable()
  {
    assertEquals(NullJavaTypeHandler.class, registry.getTypeHandler(
        Account.class).getClass());
  }


  public void testRegisterTypeHandler_Null()
  {
    try
    {
      registry.registerTypeHandler(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testRegisterTypeHandler()
  {
    CreditRatingTypeHandler th = new CreditRatingTypeHandler();
    registry.registerTypeHandler(th);

    assertSame(th, registry.getTypeHandler(CreditRating.class));
  }

}
