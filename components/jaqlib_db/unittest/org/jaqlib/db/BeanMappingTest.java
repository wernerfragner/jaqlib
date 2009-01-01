package org.jaqlib.db;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.jaqlib.AccountImpl;
import org.jaqlib.CreditRating;
import org.jaqlib.DatabaseSetup;
import org.jaqlib.db.java.typehandler.CreditRatingTypeHandler;


public class BeanMappingTest extends TestCase
{

  private BeanMapping<AccountImpl> mapping;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    mapping = new BeanMapping<AccountImpl>(AccountImpl.class);
    mapping
        .setMappingStrategy(new BeanConventionMappingStrategy());
  }


  public void testGetValue() throws SQLException
  {
    final Long id = DatabaseSetup.HUBER_ACCOUNT.getId();
    final String lastName = DatabaseSetup.HUBER_ACCOUNT.getLastName();
    DbResultSet rs = DatabaseSetup.getMockDbResultSet();

    // get account

    AccountImpl account = mapping.getValue(rs);

    // check if two fields have been filled

    assertNotNull(account);
    assertEquals(id, account.getId());
    assertEquals(lastName, account.getLastName());
  }


  public void testBeanMapping()
  {
    try
    {
      new BeanMapping<AccountImpl>(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testSetBeanFactory()
  {
    try
    {
      mapping.setBeanFactory(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testRegisterJavaTypeHandler()
  {
    try
    {
      mapping.registerJavaTypeHandler(null, new CreditRatingTypeHandler());
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
    try
    {
      mapping.registerJavaTypeHandler(String.class, null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testSetJavaTypeHandlerRegistry()
  {
    try
    {
      mapping.setJavaTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testApplyJavaTypeHandler_JavaTypeHandlerRegistered()
  {
    mapping.registerJavaTypeHandler(CreditRating.class,
        new CreditRatingTypeHandler());
    Object result = mapping.applyJavaTypeHandler(CreditRating.class,
        CreditRating.GOOD.intValue());
    assertEquals(CreditRating.GOOD, result);
  }


  public void testApplyJavaTypeHandler_NoJavaTypeHandler()
  {
    assertEquals("abc", mapping.applyJavaTypeHandler(String.class, "abc"));
  }

}