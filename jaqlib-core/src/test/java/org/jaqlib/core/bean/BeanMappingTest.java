package org.jaqlib.core.bean;

import org.jaqlib.AccountImpl;
import org.jaqlib.AccountSetup;
import org.jaqlib.CreditRating;
import org.jaqlib.CreditRatingTypeHandler;
import org.jaqlib.core.MockDsResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BeanMappingTest
{

  private BeanMapping<AccountImpl> mapping;


  @BeforeEach
  public void setUp()
  {
    mapping = new BeanMapping<>(AccountImpl.class);
    mapping.setMappingStrategy(new BeanConventionMappingStrategy());
  }


  @Test
  public void testGetValue()
  {
    final Long id = AccountSetup.HUBER_ACCOUNT.getId();
    final String lastName = AccountSetup.HUBER_ACCOUNT.getLastName();
    MockDsResultSet rs = new MockDsResultSet();

    // get account

    AccountImpl account = mapping.getValue(rs);

    // check if two fields have been filled

    assertNotNull(account);
    assertEquals(id, account.getId());
    assertEquals(lastName, account.getLastName());
  }

  @Test
  public void testBeanMapping()
  {
    try
    {
      new BeanMapping<AccountImpl>(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testSetBeanFactory()
  {
    try
    {
      mapping.setFactory(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testRegisterJavaTypeHandler()
  {
    try
    {
      mapping.registerJavaTypeHandler(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testSetJavaTypeHandlerRegistry()
  {
    try
    {
      mapping.setJavaTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testApplyJavaTypeHandler_JavaTypeHandlerRegistered()
  {
    mapping.registerJavaTypeHandler(new CreditRatingTypeHandler());
    Object result = mapping.applyJavaTypeHandler("creditRating",
        CreditRating.GOOD.intValue());
    assertEquals(CreditRating.GOOD, result);
  }

  @Test
  public void testApplyJavaTypeHandler_NoJavaTypeHandler()
  {
    assertEquals("abc", mapping.applyJavaTypeHandler("lastName", "abc"));
  }

}
