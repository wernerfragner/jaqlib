package org.jaqlib.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.jaqlib.AccountImpl;
import org.jaqlib.core.reflect.MethodInvocation;

public class CachingFetchStrategyTest extends AbstractFetchStrategyTest
{

  private DbQueryCache<AccountImpl> cache;
  private CachingFetchStrategy<AccountImpl> strategy;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    cache = new DbQueryCache<AccountImpl>(predicate);
    strategy = new CachingFetchStrategy<AccountImpl>(cache);
    initStrategy(strategy);
  }


  public void testAddResults_Collection()
  {
    EasyMock.expect(predicate.matches(accounts.get(0))).andStubReturn(true);
    EasyMock.expect(predicate.matches(accounts.get(1))).andStubReturn(true);
    EasyMock.replay(predicate);

    assertFalse(cache.isFilled());

    List<AccountImpl> results = new ArrayList<AccountImpl>();
    strategy.addResults(results);

    assertTrue(cache.isFilled());
    assertEquals(2, results.size());
    assertTrue(results.contains(accounts.get(0)));
    assertTrue(results.contains(accounts.get(1)));
    EasyMock.verify(predicate);
  }


  public void testAddResults_Map() throws NoSuchMethodException
  {
    EasyMock.expect(predicate.matches(accounts.get(0))).andStubReturn(true);
    EasyMock.expect(predicate.matches(accounts.get(1))).andStubReturn(true);
    EasyMock.replay(predicate);

    assertFalse(cache.isFilled());

    MethodInvocation invocation = getMethodInvocation();
    Map<Long, AccountImpl> results = new HashMap<Long, AccountImpl>();
    strategy.addResults(results, invocation);

    assertTrue(cache.isFilled());
    assertEquals(2, results.size());
    assertTrue(results.containsValue(accounts.get(0)));
    assertTrue(results.containsValue(accounts.get(1)));
    EasyMock.verify(predicate);
  }

}
