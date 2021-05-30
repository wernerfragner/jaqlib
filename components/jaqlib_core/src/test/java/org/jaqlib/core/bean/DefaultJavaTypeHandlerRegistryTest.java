package org.jaqlib.core.bean;

import org.jaqlib.Account;
import org.jaqlib.CreditRating;
import org.jaqlib.CreditRatingTypeHandler;
import org.jaqlib.core.bean.DefaultJavaTypeHandlerRegistry;
import org.jaqlib.core.bean.NullJavaTypeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultJavaTypeHandlerRegistryTest
{

  private DefaultJavaTypeHandlerRegistry registry;


  @BeforeEach
  protected void setUp()
  {
    registry = new DefaultJavaTypeHandlerRegistry();
  }

  @Test
  public void testGetTypeHandler_Null()
  {
    try
    {
      registry.getTypeHandler(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }


  /**
   * No java type handler for given class available. A NullJavaTypeHandler must
   * be returned.
   */
  @Test
  public void testGetTypeHandler_NoneAvailable()
  {
    assertEquals(NullJavaTypeHandler.class, registry.getTypeHandler(
        Account.class).getClass());
  }

  @Test
  public void testRegisterTypeHandler_Null()
  {
    try
    {
      registry.registerTypeHandler(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testRegisterTypeHandler()
  {
    CreditRatingTypeHandler th = new CreditRatingTypeHandler();
    registry.registerTypeHandler(th);

    assertSame(th, registry.getTypeHandler(CreditRating.class));
  }

}
