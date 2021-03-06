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

public class FirstOccurrenceFetchStrategyTest extends AbstractFetchStrategyTest
{

  private FirstOccurrenceFetchStrategy<AccountImpl> strategy;


  @BeforeEach
  public void setUp()
  {
    super.setUp();

    strategy = new FirstOccurrenceFetchStrategy<>();
    initStrategy(strategy);
  }

  @Test
  public void testAddResults_Collection()
  {
    EasyMock.expect(predicate.matches(accounts.get(0))).andStubReturn(true);
    EasyMock.expect(predicate.matches(accounts.get(1))).andStubReturn(true);
    EasyMock.replay(predicate);

    List<AccountImpl> results = new ArrayList<>();
    strategy.addResults(results);

    assertEquals(1, results.size());
    assertSame(accounts.get(0), results.get(0));
    EasyMock.verify(predicate);
  }

  @Test
  public void testAddResults_Map() throws NoSuchMethodException
  {
    EasyMock.expect(predicate.matches(accounts.get(0))).andStubReturn(true);
    EasyMock.expect(predicate.matches(accounts.get(1))).andStubReturn(true);
    EasyMock.replay(predicate);

    MethodInvocation invocation = getMethodInvocation();

    Map<Long, AccountImpl> results = new HashMap<>();
    strategy.addResults(results, invocation);

    assertEquals(1, results.size());
    assertTrue(results.containsValue(accounts.get(0)));
    EasyMock.verify(predicate);
  }

}
