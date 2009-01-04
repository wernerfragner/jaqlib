package org.jaqlib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jaqlib.core.QueryResultException;
import org.jaqlib.core.Task;
import org.jaqlib.core.WhereCondition;

/**
 * @author Werner Fragner
 */
public abstract class AbstractIterableTest<AccountType extends Account> extends
    AbstractJaqLibTest<AccountType>
{

  private void assertEqualCollections(
      Collection<? extends AccountType> expected,
      Collection<? extends AccountType> results)
  {
    assertNotNull(results);
    assertEquals(expected.size(), results.size());

    Iterator<? extends AccountType> it1 = expected.iterator();
    Iterator<? extends AccountType> it2 = results.iterator();
    while (it1.hasNext() && it2.hasNext())
    {
      assertSame(it1.next(), it2.next());
    }
  }


  @SuppressWarnings("unchecked")
  private List<AccountType> createDuplicatedElements(List<AccountType> elements)
  {
    List<AccountType> duplicatedElements = createTestAccounts();
    ((List) duplicatedElements).addAll(elements);
    return duplicatedElements;
  }


  private void assertOneCompareElement(List<AccountType> results, Double balance)
  {
    assertEquals(1, results.size());
    assertEquals(results.get(0).getBalance(), balance);
  }


  private WhereCondition<AccountType> createCompareCondition(
      final double balance)
  {
    return new WhereCondition<AccountType>()
    {

      public boolean evaluate(AccountType element)
      {
        if (element == null || element.getBalance() == null)
        {
          return false;
        }
        return element.getBalance() > balance;
      }


      @Override
      public String toString()
      {
        return "CompareBalanceCondition[expected=" + balance + "]";
      }

    };
  }


  public void testSelect_NullElement()
  {
    List<AccountType> elements = createTestAccounts();
    assertSame(elements.get(0), IterableQB.select((Class<AccountType>) null)
        .from(elements).firstResult());
  }


  /**
   * No result element class is given. Unchecked warnings are present (= not
   * type safe).
   */
  public void testSelect_NoElement()
  {
    List<?> elements = createTestAccounts();
    assertSame(elements.get(0), IterableQB.select().from(elements)
        .firstResult());
  }


  public void testSelect_toList()
  {
    List<AccountType> elements = createTestAccounts();
    List<AccountType> duplicatedElements = createDuplicatedElements(elements);

    assertEquals(elements.size() * 2, duplicatedElements.size());

    List<AccountType> results = IterableQB.select(getAccountClass()).from(
        duplicatedElements).asList();
    assertEqualCollections(duplicatedElements, results);
  }


  public void testSelect_toList_WithCondition()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond1 = createCompareCondition(500);
    WhereCondition<AccountType> cond2 = createCompareCondition(4);

    List<AccountType> results = IterableQB.select(getAccountClass()).from(
        elements).where(cond1).or(cond2).asList();
    assertEquals(2, results.size());
    assertSame(elements.get(1), results.get(0));
    assertSame(elements.get(4), results.get(1));
  }


  /**
   * Element must fulfill following restrictions:<br>
   * <tt>balance < 6 && (balance > 500 || balance > 4)</tt>.
   */
  public void testSelect_toList_WithMixedCondition()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond1 = createCompareCondition(500);
    WhereCondition<AccountType> cond2 = createCompareCondition(4);

    AccountType dummy = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select(getAccountClass()).from(
        elements).whereCall(dummy.getBalance()).isSmallerThan(6.0).and(cond1)
        .or(cond2).asList();
    assertEquals(1, results.size());
    assertSame(elements.get(4), results.get(0));
  }


  public void testSelect_toList_EmptyInput()
  {
    List<AccountType> elements = new ArrayList<AccountType>(0);

    List<AccountType> results = IterableQB.select(getAccountClass()).from(
        elements).asList();
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  public void testSelect_toSet()
  {
    List<AccountType> accounts = createTestAccounts();
    accounts = remoteNullValues(accounts);

    List<AccountType> duplicatedAccounts = new ArrayList<AccountType>(accounts);
    for (AccountType account : accounts)
    {
      duplicatedAccounts.add(account);
    }

    assertEquals(accounts.size() * 2, duplicatedAccounts.size());

    Set<AccountType> accountSet = IterableQB.select(getAccountClass()).from(
        duplicatedAccounts).asSet();
    for (AccountType account : accountSet)
    {
      assertTrue(accounts.remove(account));
    }
    // all elements must have been removed from original
    assertEquals(0, accounts.size());
  }


  private List<AccountType> remoteNullValues(List<AccountType> accounts)
  {
    return IterableQB.select(getAccountClass()).from(accounts).where()
        .element().isNotNull().asList();
  }


  public void testSelect_toSet_WithCondition()
  {
    List<? extends AccountType> elements = createTestAccounts();
    List<AccountType> duplicatedElements = new ArrayList<AccountType>(elements);
    for (AccountType element : elements)
    {
      duplicatedElements.add(element);
    }

    assertEquals(elements.size() * 2, duplicatedElements.size());

    AccountType dummy = IterableQB.getRecorder(getAccountClass());
    Set<AccountType> results = IterableQB.select(getAccountClass()).from(
        duplicatedElements).whereCall(dummy.getBalance()).isGreaterThan(4.0)
        .asSet();
    assertEquals(2, results.size());
    assertTrue(results.contains(elements.get(1)));
    assertTrue(results.contains(elements.get(4)));
  }


  public void testSelect_toSet_EmptyInput()
  {
    List<AccountType> elements = new ArrayList<AccountType>(0);

    Set<AccountType> results = IterableQB.select(getAccountClass()).from(
        elements).asSet();
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  /**
   * Null values in input collection are ignored. Null keys are supported.
   */
  public void testSelect_toMap()
  {
    List<AccountType> elements = createTestAccounts();
    List<AccountType> duplicatedElements = createDuplicatedElements(elements);

    AccountType account = IterableQB.getRecorder(getAccountClass());
    Map<Double, AccountType> results = IterableQB.select(getAccountClass())
        .from(duplicatedElements).asMap(account.getBalance());
    assertNotNull(results);
    assertEquals(4, results.size());
    assertSame(elements.get(0), results.get(1.0));
    assertSame(elements.get(1), results.get(10.0));
    assertSame(elements.get(2), results.get(null));
    assertSame(elements.get(4), results.get(5.0));
  }


  /**
   * Result of a query with a condition should be returned as map.
   */
  public void testSelect_toMap_WithCondition()
  {
    List<AccountType> elements = createTestAccounts();
    List<AccountType> duplicatedElements = createDuplicatedElements(elements);

    WhereCondition<AccountType> cond = createCompareCondition(4);

    AccountType account = IterableQB.getRecorder(getAccountClass());
    Map<Double, AccountType> results = IterableQB.select(getAccountClass())
        .from(duplicatedElements).where(cond).asMap(account.getBalance());
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(1), results.get(10.0));
    assertSame(elements.get(4), results.get(5.0));
  }


  public void testSelect_toMap_EmptyInput()
  {
    List<Account> elements = new ArrayList<Account>(0);

    Account account = IterableQB.getRecorder(Account.class);
    Map<Double, Account> results = IterableQB.select(Account.class).from(
        elements).asMap(account.getBalance());
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  public void testSelect_NullElements()
  {
    List<AccountType> elements = createTestAccounts();

    List<AccountType> result = IterableQB.select(getAccountClass()).from(
        elements).where().element().isNull().asList();
    assertEquals(2, result.size());
    for (Account element : result)
    {
      assertNull(element);
    }
  }


  public void testSelect_NullElements_WithCondition()
  {
    List<AccountType> elements = createTestAccounts();
    WhereCondition<AccountType> cond = createCompareCondition(5);

    List<AccountType> result = IterableQB.select(getAccountClass()).from(
        elements).where(cond).or().element().isNull().asList();
    assertEquals(3, result.size());
    assertNotNull(result.get(0));
    assertNull(result.get(1));
    assertNull(result.get(2));
  }


  public void testSelect_NotNullElements()
  {
    List<AccountType> elements = createTestAccounts();

    List<AccountType> result = remoteNullValues(elements);
    assertEquals(4, result.size());
    for (Account element : result)
    {
      assertNotNull(element);
    }
  }


  public void testSelect_NotNullElements_WithCondition()
  {
    List<AccountType> elements = createTestAccounts();
    WhereCondition<AccountType> cond = createCompareCondition(5);

    List<AccountType> result = IterableQB.select(getAccountClass()).from(
        elements).where(cond).and().element().isNotNull().asList();
    assertEquals(1, result.size());
    assertNotNull(result.get(0));
  }


  public void testSelect_UniqueResult_Found()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType account = IterableQB.getRecorder(getAccountClass());
    AccountType result = IterableQB.select(getAccountClass()).from(elements)
        .whereCall(account.getBalance()).isEqual(10.0).uniqueResult();
    assertNotNull(result);
    assertSame(elements.get(1), result);
  }


  public void testSelect_UniqueResult_NotFound()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType account = IterableQB.getRecorder(getAccountClass());
    AccountType result = IterableQB.select(getAccountClass()).from(elements)
        .whereCall(account.getBalance()).isEqual(100.0).uniqueResult();
    assertNull(result);
  }


  public void testSelect_UniqueResult_NotUnique()
  {
    List<AccountType> elements = createTestAccounts();
    addElement(elements, 10.0);

    AccountType account = IterableQB.getRecorder(getAccountClass());
    try
    {
      IterableQB.select(getAccountClass()).from(elements).whereCall(
          account.getBalance()).isEqual(10.0).uniqueResult();
      fail("Did not throw QueryResultException");
    }
    catch (QueryResultException e)
    {
    }
  }


  public void testSelect_FirstResult_Found()
  {
    List<AccountType> elements = createTestAccounts();
    addElement(elements, 5.0);

    AccountType account = IterableQB.getRecorder(getAccountClass());
    AccountType result = IterableQB.select(getAccountClass()).from(elements)
        .whereCall(account.getBalance()).isEqual(5.0).firstResult();
    assertNotNull(result);
    assertSame(elements.get(4), result);
  }


  public void testSelect_FirstResult_NotFound()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType account = IterableQB.getRecorder(getAccountClass());
    AccountType result = IterableQB.select(getAccountClass()).from(elements)
        .whereCall(account.getBalance()).isEqual(100.0).firstResult();
    assertNull(result);
  }


  public void testSelect_LastResult_Found()
  {
    List<AccountType> elements = createTestAccounts();
    addElement(elements, 5.0);

    AccountType account = IterableQB.getRecorder(getAccountClass());
    AccountType result = IterableQB.select(getAccountClass()).from(elements)
        .whereCall(account.getBalance()).isEqual(5.0).lastResult();
    assertNotNull(result);
    assertSame(elements.get(6), result);
  }


  public void testSelect_LastResult_NotFound()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType account = IterableQB.getRecorder(getAccountClass());
    AccountType result = IterableQB.select(getAccountClass()).from(elements)
        .whereCall(account.getBalance()).isEqual(100.0).lastResult();
    assertNull(result);
  }


  /**
   * A custom condition and a reflective method call condition are combined
   * together.
   */
  public void testSelect_MixedConditions()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond1 = createCompareCondition(2);
    Account dummy = IterableQB.getRecorder(Account.class);

    List<AccountType> results = IterableQB.select(getAccountClass()).from(
        elements).whereCall(dummy.getBalance()).isGreaterThan(5.0).and(cond1)
        .asList();

    assertOneCompareElement(results, 10.0);
  }


  /**
   * A custom condition and a reflective method call condition are combined
   * together.
   */
  public void testSelect_MultipleMethodCallConditions()
  {
    List<AccountType> elements = createTestAccounts();

    Account dummy = IterableQB.getRecorder(Account.class);
    dummy.getBalance();
    dummy.getCreditRating();

    List<AccountType> results = IterableQB.select(getAccountClass()).from(
        elements).whereCall(0.0).isGreaterThan(5.0).andCall(CreditRating.GOOD)
        .isEqual(CreditRating.GOOD).asList();

    assertOneCompareElement(results, 10.0);
  }


  public void testSelect_MultipleBooleanMethodCallConditions()
  {
    List<AccountType> elements = createTestAccounts();
    elements.get(0).setActive(false);

    Account dummy = IterableQB.getRecorder(Account.class);

    assertEquals(6, elements.size());

    List<AccountType> results = IterableQB.select(getAccountClass()).from(
        elements).whereCallIsTrue(dummy.isActive()).andCallIsTrue(
        dummy.isActive()).asList();

    assertEquals(3, results.size());
  }


  /**
   * Two custom WHERE conditions are given.
   */
  public void testSelect_TwoAndConditions()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond1 = createCompareCondition(2);
    WhereCondition<AccountType> cond2 = createCompareCondition(5);

    // check if query returns the right result

    List<AccountType> results = IterableQB.select(getAccountClass()).from(
        elements).where(cond1).and(cond2).asList();
    assertOneCompareElement(results, 10.0);

    // check if conditions can be swapped without affecting the result

    results = IterableQB.select(getAccountClass()).from(elements).where(cond2)
        .and(cond1).asList();
    assertOneCompareElement(results, 10.0);
  }


  /**
   * Immediately execute a task without any WHERE conditions.
   */
  public void testSelectExecute()
  {
    List<AccountType> elements = createTestAccounts();

    MockTask<AccountType> task = new MockTask<AccountType>();
    IterableQB.select(getAccountClass()).from(elements).execute(task);

    assertEquals(elements.size(), task.executeVisited);
  }


  /**
   * Execute task after applying various WHERE conditions.
   */
  public void testSelectExecute_WithCondition()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond = createCompareCondition(2);
    AccountType dummy = IterableQB.getRecorder(getAccountClass());

    MockTask<AccountType> task = new MockTask<AccountType>();
    List<AccountType> result = IterableQB.select(getAccountClass()).from(
        elements).where(cond).andCall(dummy.getCreditRating()).isEqual(
        CreditRating.POOR).executeWithResult(task).asList();

    assertEquals(1, result.size());
    assertEquals(1, task.executeVisited);
  }


  /**
   * Apply one condition then execute task and then apply another condition.
   * Task must be executed on results of first conditions.
   */
  public void testSelectExecute_WithNestedCondition()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond = createCompareCondition(2);
    AccountType dummy = IterableQB.getRecorder(getAccountClass());

    MockTask<AccountType> task = new MockTask<AccountType>();
    List<AccountType> result = IterableQB.select(getAccountClass()).from(
        elements).where(cond).executeWithResult(task).andCall(
        dummy.getCreditRating()).isEqual(CreditRating.POOR).asList();

    // second condtions restricts result set by one
    // --> task must be executed twice, actual result set size is one
    assertEquals(1, result.size());
    assertEquals(2, task.executeVisited);
  }


  public void testSelect_Concurrent() throws InterruptedException
  {
    ConcurrentRunnable r1 = new ConcurrentRunnable();
    ConcurrentRunnable r2 = new ConcurrentRunnable();

    Thread t1 = new Thread(r1);
    t1.setDaemon(true);
    t1.start();

    Thread t2 = new Thread(r2);
    t2.setDaemon(true);
    t2.start();

    t1.join();
    t2.join();

    assertNull(r1.getFailureMessage(), r1.getThrowable());
  }


  private class ConcurrentRunnable implements Runnable
  {

    private Throwable t;


    public void run()
    {
      try
      {
        List<AccountType> elements = createTestAccounts();
        AccountType dummy = IterableQB.getRecorder(getAccountClass());

        IterableQB.select(getAccountClass()).from(elements).whereCallIsTrue(
            dummy.isActive()).asList();
      }
      catch (Throwable t)
      {
        this.t = t;
      }
    }


    public String getFailureMessage()
    {
      if (t != null)
      {
        return "Runnable threw exception: " + t;
      }
      return "Test successfull";
    }


    public Throwable getThrowable()
    {
      return t;
    }
  }


  private static class MockTask<AccountType> implements Task<AccountType>
  {

    int executeVisited = 0;


    public void execute(AccountType element)
    {
      executeVisited++;
    }

  }

}
