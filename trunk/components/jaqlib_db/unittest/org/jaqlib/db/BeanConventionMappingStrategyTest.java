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

    strategy = new BeanConventionMappingRetrievalStrategy();
  }


  public void testExecute_Null()
  {
    try
    {
      strategy.getMappings(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testExecute()
  {
    BeanMapping<AccountImpl> mapping = new BeanMapping<AccountImpl>(
        AccountImpl.class);
    mapping.setMappingRetrievalStrategy(strategy);

    List<String> results = getResults(mapping);
    assertEquals(5, results.size());
    assertTrue(results.contains("id"));
    assertTrue(results.contains("lastName"));
    assertTrue(results.contains("firstName"));
    assertTrue(results.contains("balance"));
    assertTrue(results.contains("creditRating"));
  }


  private List<String> getResults(BeanMapping<AccountImpl> result)
  {
    List<String> results = new ArrayList<String>();
    for (AbstractMapping<?> dbSelectResult : result)
    {
      assertEquals(ColumnMapping.class, dbSelectResult.getClass());
      results.add(((ColumnMapping<?>) dbSelectResult).getColumnName());
    }
    return results;
  }

}
