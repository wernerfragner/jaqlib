package org.jaqlib;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Werner Fragner
 * 
 * @param <ResultElementType>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractJaqLibTest<ResultElementType extends SimpleTestElement>
    extends TestCase
{

  protected abstract Class<ResultElementType> getResultElementClass();


  public List<ResultElementType> createIsMatchElements()
  {
    SimpleTestElementImpl testClass1 = new SimpleTestElementImpl(false);
    SimpleTestElementImpl testClass2 = new SimpleTestElementImpl(true);
    SimpleTestElementImpl testClass3 = null;
    SimpleTestElementImpl testClass4 = new SimpleTestElementImpl(false);

    List elements = new ArrayList();
    elements.add(testClass1);
    elements.add(testClass2);
    elements.add(testClass3);
    elements.add(testClass4);
    return elements;
  }


  public List<ResultElementType> createGetObjectElements()
  {
    SimpleTestElementImpl testClass1 = new SimpleTestElementImpl(new Object());
    SimpleTestElementImpl testClass2 = new SimpleTestElementImpl(new Object());
    SimpleTestElementImpl testClass3 = null;
    SimpleTestElementImpl testClass4 = new SimpleTestElementImpl(null);

    List elements = new ArrayList();
    elements.add(testClass1);
    elements.add(testClass2);
    elements.add(testClass3);
    elements.add(testClass4);
    return elements;
  }


  public List<ResultElementType> createGetCompareElements()
  {
    SimpleTestElementImpl testClass1 = new SimpleTestElementImpl(1);
    SimpleTestElementImpl testClass2 = new SimpleTestElementImpl(10);
    SimpleTestElementImpl testClass3 = new SimpleTestElementImpl(null);
    SimpleTestElementImpl testClass4 = null;
    SimpleTestElementImpl testClass5 = new SimpleTestElementImpl(5);

    List elements = new ArrayList();
    elements.add(testClass1);
    elements.add(testClass2);
    elements.add(testClass3);
    elements.add(testClass4);
    elements.add(testClass5);
    return elements;
  }


  public List<ResultElementType> createListWithNulls()
  {
    List elements = new ArrayList();
    elements.add(new SimpleTestElementImpl());
    elements.add(null);
    elements.add(null);
    elements.add(new SimpleTestElementImpl());
    return elements;
  }


  public void addElement(List elements, int compareValue)
  {
    elements.add(new SimpleTestElementImpl(compareValue));
  }

}
