package org.jaqlib.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.jaqlib.AccountImpl;
import org.jaqlib.core.FirstOccurrenceFetchStrategy;
import org.jaqlib.core.reflect.MethodInvocation;

public class FirstOccurrenceFetchStrategyTest extends AbstractFetchStrategyTest
{

  private FirstOccurrenceFetchStrategy<AccountImpl> strategy;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    strategy = new FirstOccurrenceFetchStrategy<AccountImpl>();
    initStrategy(strategy);
  }


  public void testAddResults_Collection()
  {
    EasyMock.expect(predicate.matches(accounts.get(0))).andStubReturn(true);
    EasyMock.expect(predicate.matches(accounts.get(1))).andStubReturn(true);
    EasyMock.replay(predicate);

    List<AccountImpl> results = new ArrayList<AccountImpl>();
    strategy.addResults(results);

    assertEquals(1, results.size());
    assertSame(accounts.get(0), results.get(0));
    EasyMock.verify(predicate);
  }


  public void testAddResults_Map() throws NoSuchMethodException
  {
    EasyMock.expect(predicate.matches(accounts.get(0))).andStubReturn(true);
    EasyMock.expect(predicate.matches(accounts.get(1))).andStubReturn(true);
    EasyMock.replay(predicate);

    MethodInvocation invocation = getMethodInvocation();

    Map<Long, AccountImpl> results = new HashMap<Long, AccountImpl>();
    strategy.addResults(results, invocation);

    assertEquals(1, results.size());
    assertTrue(results.containsValue(accounts.get(0)));
    EasyMock.verify(predicate);
  }

}
