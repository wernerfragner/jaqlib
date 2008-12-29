package org.jaqlib.db;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jaqlib.AccountImpl;

public class BeanConventionMappingStrategyTest extends TestCase
{

  private BeanConventionMappingRetrievalStrategy strategy;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    strategy = new BeanConventionMappingRetrievalStrategy(AccountImpl.class);
  }


  public void testBeanConventionMappingStrategy_Null()
  {
    try
    {
      new BeanConventionMappingRetrievalStrategy(null);
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
      strategy.addMappings(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testExecute()
  {
    BeanDbSelectResult<AccountImpl> result = new BeanDbSelectResult<AccountImpl>(
        AccountImpl.class);
    strategy.addMappings(result);

    List<String> results = getResults(result);
    assertEquals(5, results.size());
    assertTrue(results.contains("id"));
    assertTrue(results.contains("lastName"));
    assertTrue(results.contains("firstName"));
    assertTrue(results.contains("balance"));
    assertTrue(results.contains("creditRating"));
  }


  private List<String> getResults(BeanDbSelectResult<AccountImpl> result)
  {
    List<String> results = new ArrayList<String>();
    for (DbSelectResult<?> dbSelectResult : result)
    {
      assertEquals(PrimitiveDbSelectResult.class, dbSelectResult.getClass());
      results.add(((PrimitiveDbSelectResult<?>) dbSelectResult).getColumnName());
    }
    return results;
  }

}
