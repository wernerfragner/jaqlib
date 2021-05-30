package org.jaqlib.core;

import org.easymock.EasyMock;
import org.jaqlib.AccountImpl;
import org.jaqlib.core.reflect.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CachingFetchStrategyTest extends AbstractFetchStrategyTest
{

  private QueryCache<AccountImpl> cache;
  private CachingFetchStrategy<AccountImpl> strategy;


  @BeforeEach
  public void setUp()
  {
    super.setUp();

    cache = new QueryCache<>(predicate);
    strategy = new CachingFetchStrategy<>(cache);
    initStrategy(strategy);
  }


  @Test
  public void testAddResults_Collection()
  {
    EasyMock.expect(predicate.matches(accounts.get(0))).andStubReturn(true);
    EasyMock.expect(predicate.matches(accounts.get(1))).andStubReturn(true);
    EasyMock.replay(predicate);

    assertFalse(cache.isFilled());

    List<AccountImpl> results = new ArrayList<>();
    strategy.addResults(results);

    assertTrue(cache.isFilled());
    assertEquals(2, results.size());
    assertTrue(results.contains(accounts.get(0)));
    assertTrue(results.contains(accounts.get(1)));
    EasyMock.verify(predicate);
  }

  @Test
  public void testAddResults_Map() throws NoSuchMethodException
  {
    EasyMock.expect(predicate.matches(accounts.get(0))).andStubReturn(true);
    EasyMock.expect(predicate.matches(accounts.get(1))).andStubReturn(true);
    EasyMock.replay(predicate);

    assertFalse(cache.isFilled());

    MethodInvocation invocation = getMethodInvocation();
    Map<Long, AccountImpl> results = new HashMap<>();
    strategy.addResults(results, invocation);

    assertTrue(cache.isFilled());
    assertEquals(2, results.size());
    assertTrue(results.containsValue(accounts.get(0)));
    assertTrue(results.containsValue(accounts.get(1)));
    EasyMock.verify(predicate);
  }

}
