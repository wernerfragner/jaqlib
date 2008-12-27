package org.jaqlib;

import java.util.List;

public abstract class AbstractReflectiveConditionTest<ResultElementType extends SimpleTestElement>
    extends AbstractJaqLibTest<ResultElementType>
{

  protected abstract ResultElementType createElement(int compareValue);


  public void testSelect_ReflectiveCondition_IsEqual()
  {
    List<ResultElementType> elements = createIsMatchElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface.isMatch()).isEqual(true).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }


  public void testSelect_ReflectiveCondition_IsGreaterThan()
  {
    ResultElementType element = createElement(5);

    List<ResultElementType> elements = createGetCompareElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface).isGreaterThan(element).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }


  public void testSelect_ReflectiveCondition_IsGreaterThanOrEqualTo()
  {
    ResultElementType element = createElement(5);

    List<ResultElementType> elements = createGetCompareElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface).isGreaterThanOrEqualTo(element).toList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(1), results.get(0));
    assertSame(elements.get(4), results.get(1));
  }


  public void testSelect_ReflectiveCondition_IsSmallerThan()
  {
    ResultElementType element = createElement(5);

    List<ResultElementType> elements = createGetCompareElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface).isSmallerThan(element).toList();
    assertNotNull(results);
    assertEquals(2, results.size());
    assertSame(elements.get(0), results.get(0));
    assertSame(elements.get(2), results.get(1));
  }


  public void testSelect_ReflectiveCondition_IsSmallerThanOrEqualTo()
  {
    ResultElementType element = createElement(5);

    List<ResultElementType> elements = createGetCompareElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface).isSmallerThanOrEqualTo(element).toList();
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
    List<ResultElementType> elements = createGetObjectElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface.getObject()).isNull().toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(3), results.get(0));
  }


  /**
   * Select all null elements in the collection.
   */
  public void testSelect_ReflectiveCondition_IsNull2()
  {
    List<ResultElementType> elements = createGetObjectElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface).isNull().toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(2), results.get(0));
  }


  /**
   * Invoke a method on the test interface and check if the result is not null.
   */
  public void testSelect_ReflectiveCondition_IsNotNull1()
  {
    List<ResultElementType> elements = createGetObjectElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface.getObject()).isNotNull().toList();
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
    List<ResultElementType> elements = createGetObjectElements();

    ResultElementType testInterface = QB
        .getMethodCallRecorder(getResultElementClass());
    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(testInterface).isNotNull().toList();
    assertNotNull(results);
    assertEquals(3, results.size());
    assertSame(elements.get(0), results.get(0));
    assertSame(elements.get(1), results.get(1));
    assertSame(elements.get(3), results.get(2));
  }


}
