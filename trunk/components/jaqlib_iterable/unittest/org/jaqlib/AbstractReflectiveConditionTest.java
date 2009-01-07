package org.jaqlib;

import java.util.List;

public abstract class AbstractReflectiveConditionTest<AccountType extends Account>
    extends AbstractJaqLibTest<AccountType>
{

  protected abstract AccountType createAccountType(Double balance);


  public void testSelect_ReflectiveCondition_IsEqual()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface.getLastName()).isEqual("maier").asList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }


  public void testSelect_ReflectiveCondition_IsGreaterThan()
  {
    AccountType element = createAccountType(5.0);

    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface).isGreaterThan(element).asList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }


  public void testSelect_ReflectiveCondition_IsGreaterThanOrEqualTo()
  {
    AccountType element = createAccountType(5.0);

    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface).isGreaterThanOrEqualTo(element).asList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(1), results.get(0));
    assertSame(elements.get(4), results.get(1));
  }


  public void testSelect_ReflectiveCondition_IsSmallerThan()
  {
    AccountType element = createAccountType(5.0);

    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface).isSmallerThan(element).asList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(0), results.get(0));
    assertSame(elements.get(2), results.get(1));
  }


  public void testSelect_ReflectiveCondition_IsSmallerThanOrEqualTo()
  {
    AccountType element = createAccountType(5.0);

    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface).isSmallerThanOrEqualTo(element).asList();
    assertNotNull(results);
    assertEquals(3, results.size());
    assertSame(elements.get(0), results.get(0));
    assertSame(elements.get(2), results.get(1));
    assertSame(elements.get(4), results.get(2));
  }


  /**
   * Invoke a method on the test interface and check if the result is null.
   */
  public void testSelect_ReflectiveCondition_IsNull1()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface.getLastName()).isNull().asList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(2), results.get(0));
    assertSame(elements.get(4), results.get(1));
  }


  /**
   * Select all null elements in the collection.
   */
  public void testSelect_ReflectiveCondition_IsNull2()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface).isNull().asList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(3), results.get(0));
    assertSame(elements.get(5), results.get(0));
  }


  /**
   * Invoke a method on the test interface and check if the result is not null.
   */
  public void testSelect_ReflectiveCondition_IsNotNull1()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface.getLastName()).isNotNull().asList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(0), results.get(0));
    assertSame(elements.get(1), results.get(1));
  }


  /**
   * Select all not null elements in the collection.
   */
  public void testSelect_ReflectiveCondition_IsNotNull2()
  {
    List<AccountType> elements = createTestAccounts();

    AccountType testInterface = IterableQB.getRecorder(getAccountClass());
    List<AccountType> results = IterableQB.select().from(elements).whereCall(
        testInterface).isNotNull().asList();
    assertNotNull(results);
    assertEquals(4, results.size());
    assertSame(elements.get(0), results.get(0));
    assertSame(elements.get(1), results.get(1));
    assertSame(elements.get(2), results.get(2));
    assertSame(elements.get(4), results.get(3));
  }


}
