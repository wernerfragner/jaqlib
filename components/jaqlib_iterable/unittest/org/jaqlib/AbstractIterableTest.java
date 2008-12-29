package org.jaqlib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jaqlib.query.WhereCondition;

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
    };
  }


  public void testSelect_NullElement()
  {
    List<AccountType> elements = createTestAccounts();
    assertSame(elements.get(0), QB.select((Class<AccountType>) null).from(
        elements).firstResult());
  }


  /**
   * No result element class is given. Unchecked warnings are present (= not
   * type safe).
   */
  @SuppressWarnings("unchecked")
  public void testSelect_NoElement()
  {
    List elements = createTestAccounts();
    assertSame(elements.get(0), QB.select().from(elements).firstResult());
  }


  public void testSelect_toList()
  {
    List<AccountType> elements = createTestAccounts();
    List<AccountType> duplicatedElements = createDuplicatedElements(elements);

    assertEquals(elements.size() * 2, duplicatedElements.size());

    List<AccountType> results = QB.select(getAccountClass()).from(
        duplicatedElements).asList();
    assertEqualCollections(duplicatedElements, results);
  }


  public void testSelect_toList_WithCondition()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond1 = createCompareCondition(500);
    WhereCondition<AccountType> cond2 = createCompareCondition(4);

    List<AccountType> results = QB.select(getAccountClass()).from(elements)
        .where(cond1).or(cond2).asList();
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

    AccountType dummy = QB.getMethodCallRecorder(getAccountClass());
    List<AccountType> results = QB.select(getAccountClass()).from(elements)
        .where(dummy.getBalance()).isSmallerThan(6.0).and(cond1).or(cond2)
        .asList();
    assertEquals(1, results.size());
    assertSame(elements.get(4), results.get(0));
  }


  public void testSelect_toList_EmptyInput()
  {
    List<AccountType> elements = new ArrayList<AccountType>(0);

    List<AccountType> results = QB.select(getAccountClass()).from(elements)
        .asList();
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

    Set<AccountType> accountSet = QB.select(getAccountClass()).from(
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
    return QB.select(getAccountClass()).from(accounts).where().element()
        .isNotNull().asList();
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

    AccountType dummy = QB.getMethodCallRecorder(getAccountClass());
    Set<AccountType> results = QB.select(getAccountClass()).from(
        duplicatedElements).where(dummy.getBalance()).isGreaterThan(4.0)
        .asSet();
    assertEquals(2, results.size());
    assertTrue(results.contains(elements.get(1)));
    assertTrue(results.contains(elements.get(4)));
  }


  public void testSelect_toSet_EmptyInput()
  {
    List<AccountType> elements = new ArrayList<AccountType>(0);

    Set<AccountType> results = QB.select(getAccountClass()).from(elements)
        .asSet();
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

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    Map<Double, AccountType> results = QB.select(getAccountClass()).from(
        duplicatedElements).asMap(account.getBalance());
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

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    Map<Double, AccountType> results = QB.select(getAccountClass()).from(
        duplicatedElements).where(cond).asMap(account.getBalance());
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(1), results.get(10.0));
    assertSame(elements.get(4), results.get(5.0));
  }


  public void testSelect_toMap_EmptyInput()
  {
    List<Account> elements = new ArrayList<Account>(0);

    Account account = QB.getMethodCallRecorder(Account.class);
    Map<Double, Account> results = QB.select(Account.class).from(elements)
        .asMap(account.getBalance());
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  public void testSelect_NullElements()
  {
    List<AccountType> elements = createTestAccounts();

    List<AccountType> result = QB.select(getAccountClass()).from(elements)
        .where().element().isNull().asList();
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

    List<AccountType> result = QB.select(getAccountClass()).from(elements)
        .where(cond).or().element().isNull().asList();
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

    List<AccountType> result = QB.select(getAccountClass()).from(elements)
        .where(cond).and().element().isNotNull().asList();
    assertEquals(1, result.size());
    assertNotNull(result.get(0));
  }


  public void testSelect_UniqueResult_Found()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    AccountType result = QB.select(getAccountClass()).from(elements).where(
        account.getBalance()).isEqual(10.0).uniqueResult();
    assertNotNull(result);
    assertSame(elements.get(1), result);
  }


  public void testSelect_UniqueResult_NotFound()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    AccountType result = QB.select(getAccountClass()).from(elements).where(
        account.getBalance()).isEqual(100.0).uniqueResult();
    assertNull(result);
  }


  public void testSelect_UniqueResult_NotUnique()
  {
    List<AccountType> elements = createTestAccounts();
    addElement(elements, 10.0);

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    try
    {
      QB.select(getAccountClass()).from(elements).where(account.getBalance())
          .isEqual(10.0).uniqueResult();
      fail("Did not throw IllegalStateException");
    }
    catch (IllegalStateException ise)
    {
    }
  }


  public void testSelect_FirstResult_Found()
  {
    List<AccountType> elements = createTestAccounts();
    addElement(elements, 5.0);

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    AccountType result = QB.select(getAccountClass()).from(elements).where(
        account.getBalance()).isEqual(5.0).firstResult();
    assertNotNull(result);
    assertSame(elements.get(4), result);
  }


  public void testSelect_FirstResult_NotFound()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    AccountType result = QB.select(getAccountClass()).from(elements).where(
        account.getBalance()).isEqual(100.0).firstResult();
    assertNull(result);
  }


  public void testSelect_LastResult_Found()
  {
    List<AccountType> elements = createTestAccounts();
    addElement(elements, 5.0);

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    AccountType result = QB.select(getAccountClass()).from(elements).where(
        account.getBalance()).isEqual(5.0).lastResult();
    assertNotNull(result);
    assertSame(elements.get(6), result);
  }


  public void testSelect_LastResult_NotFound()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType account = QB.getMethodCallRecorder(getAccountClass());
    AccountType result = QB.select(getAccountClass()).from(elements).where(
        account.getBalance()).isEqual(100.0).lastResult();
    assertNull(result);
  }


  /**
   * A user-defined condition and a reflective method call condition are
   * combined together.
   */
  public void testSelect_MixedConditions()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond1 = createCompareCondition(2);
    Account dummy = QB.getMethodCallRecorder(Account.class);

    List<AccountType> results = QB.select(getAccountClass()).from(elements)
        .where(dummy.getBalance()).isGreaterThan(5.0).and(cond1).asList();

    assertOneCompareElement(results, 10.0);
  }


  /**
   * A user-defined condition and a reflective method call condition are
   * combined together.
   */
  public void testSelect_MultipleMethodCallConditions()
  {
    List<AccountType> elements = createTestAccounts();

    Account dummy = QB.getMethodCallRecorder(Account.class);
    dummy.getBalance();
    dummy.getCreditRating();

    List<AccountType> results = QB.select(getAccountClass()).from(elements)
        .where(0.0).isGreaterThan(5.0).andMethodCallResult(CreditRating.POOR)
        .isEqual(CreditRating.POOR).asList();

    assertOneCompareElement(results, 10.0);
  }


  /**
   * Two user-defined where conditions are given.
   */
  public void testSelect_TwoAndConditions()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> cond1 = createCompareCondition(2);
    WhereCondition<AccountType> cond2 = createCompareCondition(5);

    // check if query returns the right result

    List<AccountType> results = QB.select(getAccountClass()).from(elements)
        .where(cond1).and(cond2).asList();
    assertOneCompareElement(results, 10.0);

    // check if conditions can be swapped without affecting the result

    results = QB.select(getAccountClass()).from(elements).where(cond2).and(
        cond1).asList();
    assertOneCompareElement(results, 10.0);
  }


}
