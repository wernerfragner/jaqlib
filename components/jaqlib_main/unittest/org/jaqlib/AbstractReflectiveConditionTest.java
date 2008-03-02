package org.jaqlib;

import java.util.List;

public abstract class AbstractReflectiveConditionTest<ResultItemType extends SimpleTestItem>
    extends AbstractJaqLibTest<ResultItemType>
{

  protected abstract ResultItemType createItem(int compareValue);


  public void testSelect_ReflectiveCondition_IsEqual()
  {
    List<? extends ResultItemType> items = createIsMatchItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface.isMatch()).isEqual(true).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(items.get(1), results.get(0));
  }


  public void testSelect_ReflectiveCondition_IsGreaterThan()
  {
    ResultItemType item = createItem(5);

    List<? extends ResultItemType> items = createGetCompareItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface).isGreaterThan(item).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(items.get(1), results.get(0));
  }


  public void testSelect_ReflectiveCondition_IsGreaterThanOrEqualTo()
  {
    ResultItemType item = createItem(5);

    List<? extends ResultItemType> items = createGetCompareItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface).isGreaterThanOrEqualTo(item).toList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(items.get(1), results.get(0));
    assertSame(items.get(4), results.get(1));
  }


  public void testSelect_ReflectiveCondition_IsSmallerThan()
  {
    ResultItemType item = createItem(5);

    List<? extends ResultItemType> items = createGetCompareItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface).isSmallerThan(item).toList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(items.get(0), results.get(0));
    assertSame(items.get(2), results.get(1));
  }


  public void testSelect_ReflectiveCondition_IsSmallerThanOrEqualTo()
  {
    ResultItemType item = createItem(5);

    List<? extends ResultItemType> items = createGetCompareItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface).isSmallerThanOrEqualTo(item).toList();
    assertNotNull(results);
    assertEquals(3, results.size());
    assertSame(items.get(0), results.get(0));
    assertSame(items.get(2), results.get(1));
    assertSame(items.get(4), results.get(2));
  }


  /**
   * Invoke a method on the test interface and check if the result is null.
   */
  public void testSelect_ReflectiveCondition_IsNull1()
  {
    List<? extends ResultItemType> items = createGetObjectItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface.getObject()).isNull().toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(items.get(3), results.get(0));
  }


  /**
   * Select all null items in the collection.
   */
  public void testSelect_ReflectiveCondition_IsNull2()
  {
    List<? extends ResultItemType> items = createGetObjectItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface).isNull().toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(items.get(2), results.get(0));
  }


  /**
   * Invoke a method on the test interface and check if the result is not null.
   */
  public void testSelect_ReflectiveCondition_IsNotNull1()
  {
    List<? extends ResultItemType> items = createGetObjectItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface.getObject()).isNotNull().toList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(items.get(0), results.get(0));
    assertSame(items.get(1), results.get(1));
  }


  /**
   * Select all not null items in the collection.
   */
  public void testSelect_ReflectiveCondition_IsNotNull2()
  {
    List<? extends ResultItemType> items = createGetObjectItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    List<? extends ResultItemType> results = QB.select(getResultItemClass())
        .from(items).where(testInterface).isNotNull().toList();
    assertNotNull(results);
    assertEquals(3, results.size());
    assertSame(items.get(0), results.get(0));
    assertSame(items.get(1), results.get(1));
    assertSame(items.get(3), results.get(2));
  }


}
