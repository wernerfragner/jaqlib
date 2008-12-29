package org.jaqlib.db;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jaqlib.AccountImpl;

public class BeanConventionMappingStrategyTest extends TestCase
{

  private BeanConventionMappingStrategy strategy;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    strategy = new BeanConventionMappingStrategy(AccountImpl.class);
  }


  public void testBeanConventionMappingStrategy_Null()
  {
    try
    {
      new BeanConventionMappingStrategy(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testExecute_Null()
  {
    try
    {
      strategy.execute(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testExecute()
  {
    ComplexDbSelectResult<AccountImpl> result = new ComplexDbSelectResult<AccountImpl>(
        AccountImpl.class);
    strategy.execute(result);

    List<String> results = getResults(result);
    assertEquals(5, results.size());
    assertTrue(results.contains("id"));
    assertTrue(results.contains("lastName"));
    assertTrue(results.contains("firstName"));
    assertTrue(results.contains("balance"));
    assertTrue(results.contains("creditRating"));
  }


  private List<String> getResults(ComplexDbSelectResult<AccountImpl> result)
  {
    List<String> results = new ArrayList<String>();
    for (DbSelectResult<?> dbSelectResult : result)
    {
      assertEquals(SingleDbSelectResult.class, dbSelectResult.getClass());
      results.add(((SingleDbSelectResult<?>) dbSelectResult).getColumnName());
    }
    return results;
  }

}
