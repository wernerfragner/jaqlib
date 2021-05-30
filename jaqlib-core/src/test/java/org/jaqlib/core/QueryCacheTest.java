package org.jaqlib.core;

import org.easymock.EasyMock;
import org.jaqlib.Account;
import org.jaqlib.AccountImpl;
import org.jaqlib.core.reflect.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QueryCacheTest
{

  private ElementPredicate<Account> predicate;
  private QueryCache<Account> cache;


  @BeforeEach
  public void setUp()
  {
    predicate = EasyMock.createMock(ElementPredicate.class);
    cache = new QueryCache<>(predicate);
  }


  @Test
  public void testDatabaseQueryCache_Null()
  {
    try
    {
      new QueryCache<Account>(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testIsFilled()
  {
    assertFalse(cache.isFilled());
    cache.setFilled();
    assertTrue(cache.isFilled());
  }

  @Test
  public void testAddResults_Collection()
  {
    // set up

    AccountImpl account1 = addAccount();
    AccountImpl account2 = addAccount();
    AccountImpl account3 = addAccount();

    EasyMock.expect(predicate.matches(account1)).andReturn(false);
    EasyMock.expect(predicate.matches(account2)).andReturn(true);
    EasyMock.expect(predicate.matches(account3)).andReturn(false);
    EasyMock.replay(predicate);

    // test

    List<Account> results = new ArrayList<>();
    cache.addResults(results);

    // verify

    assertEquals(1, results.size());
    assertSame(account2, results.get(0));
    EasyMock.verify(predicate);
  }


  private AccountImpl addAccount()
  {
    AccountImpl account = new AccountImpl();
    cache.add(account);
    return account;
  }

  @Test
  public void testAddResults_Map() throws NoSuchMethodException
  {
    // set up

    AccountImpl account1 = addAccount();
    AccountImpl account2 = addAccount();
    AccountImpl account3 = addAccount();

    EasyMock.expect(predicate.matches(account1)).andReturn(false);
    EasyMock.expect(predicate.matches(account2)).andReturn(true);
    EasyMock.expect(predicate.matches(account3)).andReturn(false);
    EasyMock.replay(predicate);

    MethodInvocation invocation = getMethodInvocation();

    // test

    Map<Long, Account> results = new HashMap<>();
    cache.addResults(results, invocation);

    // verify

    assertEquals(1, results.size());
    assertTrue(results.containsValue(account2));

    EasyMock.verify(predicate);
  }


  private MethodInvocation getMethodInvocation() throws NoSuchMethodException
  {
    Method method = AccountImpl.class.getMethod("getId");
    return new MethodInvocation(method, new Object[0]);
  }


}
