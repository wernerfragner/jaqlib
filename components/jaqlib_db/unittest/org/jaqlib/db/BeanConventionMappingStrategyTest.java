package org.jaqlib.db;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jaqlib.AccountImpl;
import org.jaqlib.db.AbstractMapping;
import org.jaqlib.db.BeanConventionMappingRetrievalStrategy;
import org.jaqlib.db.BeanMapping;
import org.jaqlib.db.ColumnMapping;

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
      strategy.addMappings(null, new BeanMapping<AccountImpl>(
          AccountImpl.class));
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }

    try
    {
      strategy.addMappings(AccountImpl.class, null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testExecute()
  {
    BeanMapping<AccountImpl> result = new BeanMapping<AccountImpl>(
        AccountImpl.class);
    strategy.addMappings(AccountImpl.class, result);

    List<String> results = getResults(result);
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
      results
          .add(((ColumnMapping<?>) dbSelectResult).getColumnName());
    }
    return results;
  }

}
