package org.jaqlib.db;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jaqlib.AccountImpl;
import org.jaqlib.query.db.BeanConventionMappingRetrievalStrategy;
import org.jaqlib.query.db.BeanDbSelectResult;
import org.jaqlib.query.db.DbSelectResult;
import org.jaqlib.query.db.PrimitiveDbSelectResult;

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
      strategy.addMappings(null, new BeanDbSelectResult<AccountImpl>(
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
    BeanDbSelectResult<AccountImpl> result = new BeanDbSelectResult<AccountImpl>(
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


  private List<String> getResults(BeanDbSelectResult<AccountImpl> result)
  {
    List<String> results = new ArrayList<String>();
    for (DbSelectResult<?> dbSelectResult : result)
    {
      assertEquals(PrimitiveDbSelectResult.class, dbSelectResult.getClass());
      results
          .add(((PrimitiveDbSelectResult<?>) dbSelectResult).getColumnName());
    }
    return results;
  }

}
