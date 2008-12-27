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
public abstract class AbstractSelectTest<ResultItemType extends SimpleTestItem>
    extends AbstractJaqLibTest<ResultItemType>
{

  private void assertEqualCollections(
      Collection<? extends ResultItemType> expected,
      Collection<? extends ResultItemType> results)
  {
    assertNotNull(results);
    assertEquals(expected.size(), results.size());

    Iterator<? extends ResultItemType> it1 = expected.iterator();
    Iterator<? extends ResultItemType> it2 = results.iterator();
    while (it1.hasNext() && it2.hasNext())
    {
      assertSame(it1.next(), it2.next());
    }
  }


  public void testSelect_NullItem()
  {
    List<? extends ResultItemType> items = createGetCompareItems();
    assertSame(items.get(0), QB.select((Class<ResultItemType>) null)
        .from(items).firstResult());
  }


  @SuppressWarnings("unchecked")
  public void testSelect_NoItem()
  {
    List<? extends ResultItemType> items = createGetCompareItems();
    assertSame(items.get(0), QB.select().from(items).firstResult());
  }


  @SuppressWarnings("unchecked")
  public void testSelect_MultipleItems()
  {
    try
    {
      QB.select(getResultItemClass(), getResultItemClass());
      fail("Did not throw IllegalStateException!");
    }
    catch (IllegalStateException ise)
    {
    }
  }


  @SuppressWarnings("unchecked")
  public void testSelect_toList()
  {
    List<? extends ResultItemType> items = createGetCompareItems();
    List<? extends ResultItemType> duplicatedItems = createGetCompareItems();
    ((List) duplicatedItems).addAll(items);

    assertEquals(items.size() * 2, duplicatedItems.size());

    List<ResultItemType> results = QB.select(getResultItemClass()).from(
        duplicatedItems).toList();
    assertEqualCollections(duplicatedItems, results);
  }


  public void testSelect_toList_EmptyInput()
  {
    List<ResultItemType> items = new ArrayList<ResultItemType>(0);

    List<ResultItemType> results = QB.select(getResultItemClass()).from(items)
        .toList();
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  public void testSelect_toSet()
  {
    List<? extends ResultItemType> items = createGetCompareItems();
    List<ResultItemType> duplicatedItems = new ArrayList<ResultItemType>(items);
    for (ResultItemType item : items)
    {
      duplicatedItems.add(item);
    }

    assertEquals(items.size() * 2, duplicatedItems.size());

    Set<ResultItemType> results = QB.select(getResultItemClass()).from(
        duplicatedItems).toSet();
    for (ResultItemType simpleTestItem : results)
    {
      assertTrue(items.remove(simpleTestItem));
    }
    // all items must have been in set
    assertEquals(0, items.size());
  }


  public void testSelect_toSet_EmptyInput()
  {
    List<ResultItemType> items = new ArrayList<ResultItemType>(0);

    Set<ResultItemType> results = QB.select(getResultItemClass()).from(items)
        .toSet();
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  /**
   * Null values in input collection are ignored. Null keys are supported.
   */
  @SuppressWarnings("unchecked")
  public void testSelect_toMap()
  {
    List<? extends ResultItemType> items = createGetCompareItems();
    List<? extends ResultItemType> duplicatedItems = createGetCompareItems();
    ((List) duplicatedItems).addAll(items);

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    Map<Integer, ResultItemType> results = QB.select(getResultItemClass())
        .from(items).toMap(testInterface.getCompareValue());
    assertNotNull(results);
    assertEquals(4, results.size());
    assertSame(items.get(0), results.get(1));
    assertSame(items.get(1), results.get(10));
    assertSame(items.get(2), results.get(null));
    assertSame(items.get(4), results.get(5));
  }


  public void testSelect_toMap_EmptyInput()
  {
    List<SimpleTestItem> items = new ArrayList<SimpleTestItem>(0);

    SimpleTestItem testInterface = QB
        .getMethodCallRecorder(SimpleTestItem.class);
    Map<Integer, SimpleTestItem> results = QB.select(SimpleTestItem.class)
        .from(items).toMap(testInterface.getCompareValue());
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  public void testSelect_NullItems()
  {
    List<? extends ResultItemType> items = createListWithNulls();

    List<? extends ResultItemType> result = QB.select(getResultItemClass())
        .from(items).where().item().isNull().toList();
    assertEquals(2, result.size());
    for (SimpleTestItem item : result)
    {
      assertNull(item);
    }
  }


  public void testSelect_NotNullItems()
  {
    List<? extends ResultItemType> items = createListWithNulls();

    List<? extends ResultItemType> result = QB.select(getResultItemClass())
        .from(items).where().item().isNotNull().toList();
    assertEquals(2, result.size());
    for (SimpleTestItem item : result)
    {
      assertNotNull(item);
    }
  }


  public void testSelect_UniqueResult_Found()
  {
    List<? extends ResultItemType> items = createGetCompareItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    ResultItemType result = QB.select(getResultItemClass()).from(items).where(
        testInterface.getCompareValue()).isEqual(10).uniqueResult();
    assertNotNull(result);
    assertSame(items.get(1), result);
  }


  public void testSelect_UniqueResult_NotFound()
  {
    List<? extends ResultItemType> items = createGetCompareItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    ResultItemType result = QB.select(getResultItemClass()).from(items).where(
        testInterface.getCompareValue()).isEqual(100).uniqueResult();
    assertNull(result);
  }


  public void testSelect_UniqueResult_NotUnique()
  {
    List<? extends ResultItemType> items = createGetCompareItems();
    addItem(items, 10);

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    try
    {
      QB.select(SimpleTestItem.class).from(items).where(
          testInterface.getCompareValue()).isEqual(10).uniqueResult();
      fail("Did not throw IllegalStateException");
    }
    catch (IllegalStateException ise)
    {
    }
  }


  public void testSelect_FirstResult_Found()
  {
    List<? extends ResultItemType> items = createGetCompareItems();
    addItem(items, 5);

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    ResultItemType result = QB.select(getResultItemClass()).from(items).where(
        testInterface.getCompareValue()).isEqual(5).firstResult();
    assertNotNull(result);
    assertSame(items.get(4), result);
  }


  public void testSelect_FirstResult_NotFound()
  {
    List<? extends ResultItemType> items = createGetCompareItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    ResultItemType result = QB.select(getResultItemClass()).from(items).where(
        testInterface.getCompareValue()).isEqual(100).firstResult();
    assertNull(result);
  }


  public void testSelect_LastResult_Found()
  {
    List<? extends ResultItemType> items = createGetCompareItems();
    addItem(items, 5);

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    ResultItemType result = QB.select(getResultItemClass()).from(items).where(
        testInterface.getCompareValue()).isEqual(5).lastResult();
    assertNotNull(result);
    assertSame(items.get(5), result);
  }


  public void testSelect_LastResult_NotFound()
  {
    List<? extends ResultItemType> items = createGetCompareItems();

    ResultItemType testInterface = QB
        .getMethodCallRecorder(getResultItemClass());
    ResultItemType result = QB.select(getResultItemClass()).from(items).where(
        testInterface.getCompareValue()).isEqual(100).lastResult();
    assertNull(result);
  }


  public void testSelect_AndCondition()
  {
    List<SimpleTestItem> items = (List<SimpleTestItem>) createGetCompareItems();

    WhereCondition<SimpleTestItem> cond1 = new WhereCondition<SimpleTestItem>()
    {

      public boolean evaluate(SimpleTestItem item)
      {
        if (item == null)
        {
          return false;
        }
        return item.getCompareValue() > 2;
      }
    };


    WhereCondition<SimpleTestItem> cond2 = new WhereCondition<SimpleTestItem>()
    {

      public boolean evaluate(SimpleTestItem item)
      {
        if (item == null)
        {
          return false;
        }
        return item.getCompareValue() > 5;
      }
    };

    QB.select(SimpleTestItem.class).from(items).where(cond1);//.and(cond2).toList
                                                             // ();

  }


}
