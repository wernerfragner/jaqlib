package org.jaqlib;

import org.jaqlib.core.bean.BeanConventionMappingStrategy;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.FieldMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BeanConventionMappingStrategyTest
{

  private BeanConventionMappingStrategy strategy;


  @BeforeEach
  public void setUp()
  {
    strategy = new BeanConventionMappingStrategy();
  }


  @Test
  public void testExecute_Null()
  {
    try
    {
      strategy.getMappings(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testExecute()
  {
    BeanMapping<AccountImpl> mapping = new BeanMapping<>(AccountImpl.class);
    mapping.setMappingStrategy(strategy);

    List<String> results = getResults(mapping);
    assertEquals(7, results.size());
    assertTrue(results.contains("id"));
    assertTrue(results.contains("lastName"));
    assertTrue(results.contains("firstName"));
    assertTrue(results.contains("balance"));
    assertTrue(results.contains("creditRating"));
    assertTrue(results.contains("active"));
    assertTrue(results.contains("transactions"));
  }


  private List<String> getResults(BeanMapping<AccountImpl> result)
  {
    List<String> results = new ArrayList<>();
    for (FieldMapping<?> fieldMapping : result)
    {
      results.add(fieldMapping.getTargetName());
    }
    return results;
  }

}
