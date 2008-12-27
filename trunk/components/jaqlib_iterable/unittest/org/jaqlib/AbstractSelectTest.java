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
public abstract class AbstractSelectTest<ResultElementType extends SimpleTestElement>
    extends AbstractJaqLibTest<ResultElementType>
{

  private void assertEqualCollections(
      Collection<? extends ResultElementType> expected,
      Collection<? extends ResultElementType> results)
  {
    assertNotNull(results);
    assertEquals(expected.size(), results.size());

    Iterator<? extends ResultElementType> it1 = expected.iterator();
    Iterator<? extends ResultElementType> it2 = results.iterator();
    while (it1.hasNext() && it2.hasNext())
    {
      assertSame(it1.next(), it2.next());
    }
  }


  public void testSelect_NullElement()
  {
    List<ResultElementType> elements = createGetCompareElements();
    assertSame(elements.get(0), QB.select((Class<ResultElementType>) null)
        .from(elements).firstResult());
  }


  @SuppressWarnings("unchecked")
  public void testSelect_NoElement()
  {
    List elements = createGetCompareElements();
    assertSame(elements.get(0), QB.select().from(elements).firstResult());
  }


  @SuppressWarnings("unchecked")
  public void testSelect_toList()
  {
    List<ResultElementType> elements = createGetCompareElements();
    List<ResultElementType> duplicatedElements = createGetCompareElements();
    ((List) duplicatedElements).addAll(elements);

    assertEquals(elements.size() * 2, duplicatedElements.size());

    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        duplicatedElements).toList();
    assertEqualCollections(duplicatedElements, results);
  }


  public void testSelect_toList_WithCondition()
  {
    List<ResultElementType> elements = createGetCompareElements();

    WhereCondition<ResultElementType> cond1 = createCompareCondition(500);
    WhereCondition<ResultElementType> cond2 = createCompareCondition(4);

    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(cond1).or(cond2).toList();
    assertEquals(2, results.size());
    assertSame(elements.get(1), results.get(0));
    assertSame(elements.get(4), results.get(1));
  }


  /**
   * Element must fulfill following restrictions:<br>
   * <tt>compareValue < 6</li> && (compare > 500 || compare > 4)</tt>.
   */
  public void testSelect_toList_WithMixedCondition()
  {
    List<ResultElementType> elements = createGetCompareElements();

    WhereCondition<ResultElementType> cond1 = createCompareCondition(500);
    WhereCondition<ResultElementType> cond2 = createCompareCondition(4);

    ResultElementType dummy = QB.getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(dummy.getCompareValue()).isSmallerThan(6).and(cond1)
        .or(cond2).toList();
    assertEquals(1, results.size());
    assertSame(elements.get(4), results.get(0));
  }


  public void testSelect_toList_EmptyInput()
  {
    List<ResultElementType> elements = new ArrayList<ResultElementType>(0);

    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).toList();
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  public void testSelect_toSet()
  {
    List<? extends ResultElementType> elements = createGetCompareElements();
    List<ResultElementType> duplicatedElements = new ArrayList<ResultElementType>(
        elements);
    for (ResultElementType element : elements)
    {
      duplicatedElements.add(element);
    }

    assertEquals(elements.size() * 2, duplicatedElements.size());

    Set<ResultElementType> results = QB.select(getResultElementClass()).from(
        duplicatedElements).toSet();
    for (ResultElementType simpleTestElement : results)
    {
      assertTrue(elements.remove(simpleTestElement));
    }
    // all elements must have been removed from original
    assertEquals(0, elements.size());
  }


  public void testSelect_toSet_WithCondition()
  {
    List<? extends ResultElementType> elements = createGetCompareElements();
    List<ResultElementType> duplicatedElements = new ArrayList<ResultElementType>(
        elements);
    for (ResultElementType element : elements)
    {
      duplicatedElements.add(element);
    }

    assertEquals(elements.size() * 2, duplicatedElements.size());

    ResultElementType dummy = QB.getMethodCallRecorder(getResultElementClass());
    Set<ResultElementType> results = QB.select(getResultElementClass()).from(
        duplicatedElements).where(dummy.getCompareValue()).isGreaterThan(4)
        .toSet();
    assertEquals(2, results.size());
    assertTrue(results.contains(elements.get(1)));
    assertTrue(results.contains(elements.get(4)));
  }


  public void testSelect_toSet_EmptyInput()
  {
    List<ResultElementType> elements = new ArrayList<ResultElementType>(0);

    Set<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).toSet();
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  /**
   * Null values in input collection are ignored. Null keys are supported.
   */
  @SuppressWarnings("unchecked")
  public void testSelect_toMap()
  {
    List<ResultElementType> elements = createGetCompareElements();
    List<ResultElementType> duplicatedElements = createGetCompareElements();
    ((List) duplicatedElements).addAll(elements);

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    Map<Integer, ResultElementType> results = QB
        .select(getResultElementClass()).from(elements).toMap(
            testInterface.getCompareValue());
    assertNotNull(results);
    assertEquals(4, results.size());
    assertSame(elements.get(0), results.get(1));
    assertSame(elements.get(1), results.get(10));
    assertSame(elements.get(2), results.get(null));
    assertSame(elements.get(4), results.get(5));
  }


  /**
   * Result of a query with a condition should be returned as map.
   */
  @SuppressWarnings("unchecked")
  public void testSelect_toMap_WithCondition()
  {
    List<ResultElementType> elements = createGetCompareElements();
    List<ResultElementType> duplicatedElements = createGetCompareElements();
    ((List) duplicatedElements).addAll(elements);

    WhereCondition<ResultElementType> cond = createCompareCondition(4);

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    Map<Integer, ResultElementType> results = QB
        .select(getResultElementClass()).from(elements).where(cond).toMap(
            testInterface.getCompareValue());
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(1), results.get(10));
    assertSame(elements.get(4), results.get(5));
  }


  public void testSelect_toMap_EmptyInput()
  {
    List<SimpleTestElement> elements = new ArrayList<SimpleTestElement>(0);

    SimpleTestElement testInterface = QB
        .getMethodCallRecorder(SimpleTestElement.class);
    Map<Integer, SimpleTestElement> results = QB
        .select(SimpleTestElement.class).from(elements).toMap(
            testInterface.getCompareValue());
    assertNotNull(results);
    assertEquals(0, results.size());
  }


  public void testSelect_NullElements()
  {
    List<ResultElementType> elements = createListWithNulls();

    List<ResultElementType> result = QB.select(getResultElementClass()).from(
        elements).where().element().isNull().toList();
    assertEquals(2, result.size());
    for (SimpleTestElement element : result)
    {
      assertNull(element);
    }
  }


  public void testSelect_NotNullElements()
  {
    List<ResultElementType> elements = createListWithNulls();

    List<ResultElementType> result = QB.select(getResultElementClass()).from(
        elements).where().element().isNotNull().toList();
    assertEquals(2, result.size());
    for (SimpleTestElement element : result)
    {
      assertNotNull(element);
    }
  }


  public void testSelect_UniqueResult_Found()
  {
    List<ResultElementType> elements = createGetCompareElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    ResultElementType result = QB.select(getResultElementClass())
        .from(elements).where(testInterface.getCompareValue()).isEqual(10)
        .uniqueResult();
    assertNotNull(result);
    assertSame(elements.get(1), result);
  }


  public void testSelect_UniqueResult_NotFound()
  {
    List<ResultElementType> elements = createGetCompareElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    ResultElementType result = QB.select(getResultElementClass())
        .from(elements).where(testInterface.getCompareValue()).isEqual(100)
        .uniqueResult();
    assertNull(result);
  }


  public void testSelect_UniqueResult_NotUnique()
  {
    List<ResultElementType> elements = createGetCompareElements();
    addElement(elements, 10);

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    try
    {
      QB.select(getResultElementClass()).from(elements).where(
          testInterface.getCompareValue()).isEqual(10).uniqueResult();
      fail("Did not throw IllegalStateException");
    }
    catch (IllegalStateException ise)
    {
    }
  }


  public void testSelect_FirstResult_Found()
  {
    List<ResultElementType> elements = createGetCompareElements();
    addElement(elements, 5);

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    ResultElementType result = QB.select(getResultElementClass())
        .from(elements).where(testInterface.getCompareValue()).isEqual(5)
        .firstResult();
    assertNotNull(result);
    assertSame(elements.get(4), result);
  }


  public void testSelect_FirstResult_NotFound()
  {
    List<ResultElementType> elements = createGetCompareElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    ResultElementType result = QB.select(getResultElementClass())
        .from(elements).where(testInterface.getCompareValue()).isEqual(100)
        .firstResult();
    assertNull(result);
  }


  public void testSelect_LastResult_Found()
  {
    List<ResultElementType> elements = createGetCompareElements();
    addElement(elements, 5);

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    ResultElementType result = QB.select(getResultElementClass())
        .from(elements).where(testInterface.getCompareValue()).isEqual(5)
        .lastResult();
    assertNotNull(result);
    assertSame(elements.get(5), result);
  }


  public void testSelect_LastResult_NotFound()
  {
    List<ResultElementType> elements = createGetCompareElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    ResultElementType result = QB.select(getResultElementClass())
        .from(elements).where(testInterface.getCompareValue()).isEqual(100)
        .lastResult();
    assertNull(result);
  }


  /**
   * A user-defined condition and a reflective method call condition are
   * combined together.
   */
  public void testSelect_MixedConditions()
  {
    List<ResultElementType> elements = createGetCompareElements();

    WhereCondition<ResultElementType> cond1 = createCompareCondition(2);
    SimpleTestElement dummy = QB.getMethodCallRecorder(SimpleTestElement.class);

    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(dummy.getCompareValue()).isGreaterThan(5).and(cond1)
        .toList();

    assertOneCompareElement(results, 10);
  }


  /**
   * Two user-defined where conditions are given.
   */
  public void testSelect_TwoAndCondition()
  {
    List<ResultElementType> elements = createGetCompareElements();

    WhereCondition<ResultElementType> cond1 = createCompareCondition(2);
    WhereCondition<ResultElementType> cond2 = createCompareCondition(5);

    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(cond2).and(cond1).toList();

    assertOneCompareElement(results, 10);
  }


  private void assertOneCompareElement(List<ResultElementType> results,
      Integer compareValue)
  {
    assertEquals(1, results.size());
    assertEquals(results.get(0).getCompareValue(), compareValue);
  }


  private WhereCondition<ResultElementType> createCompareCondition(
      final int compareValue)
  {
    return new WhereCondition<ResultElementType>()
    {

      public boolean evaluate(ResultElementType element)
      {
        if (element == null || element.getCompareValue() == null)
        {
          return false;
        }
        return element.getCompareValue() > compareValue;
      }
    };
  }
}
