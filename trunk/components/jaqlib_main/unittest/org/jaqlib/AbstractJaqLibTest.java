package org.jaqlib;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Werner Fragner
 * 
 * @param <ResultItemType>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractJaqLibTest<ResultItemType extends SimpleTestItem>
    extends TestCase
{

  protected abstract Class<ResultItemType> getResultItemClass();


  public List<? extends ResultItemType> createIsMatchItems()
  {
    SimpleTestItemImpl testClass1 = new SimpleTestItemImpl(false);
    SimpleTestItemImpl testClass2 = new SimpleTestItemImpl(true);
    SimpleTestItemImpl testClass3 = null;
    SimpleTestItemImpl testClass4 = new SimpleTestItemImpl(false);

    List items = new ArrayList();
    items.add(testClass1);
    items.add(testClass2);
    items.add(testClass3);
    items.add(testClass4);
    return items;
  }


  public List<? extends ResultItemType> createGetObjectItems()
  {
    SimpleTestItemImpl testClass1 = new SimpleTestItemImpl(new Object());
    SimpleTestItemImpl testClass2 = new SimpleTestItemImpl(new Object());
    SimpleTestItemImpl testClass3 = null;
    SimpleTestItemImpl testClass4 = new SimpleTestItemImpl(null);

    List items = new ArrayList();
    items.add(testClass1);
    items.add(testClass2);
    items.add(testClass3);
    items.add(testClass4);
    return items;
  }


  public List<? extends ResultItemType> createGetCompareItems()
  {
    SimpleTestItemImpl testClass1 = new SimpleTestItemImpl(1);
    SimpleTestItemImpl testClass2 = new SimpleTestItemImpl(10);
    SimpleTestItemImpl testClass3 = new SimpleTestItemImpl(null);
    SimpleTestItemImpl testClass4 = null;
    SimpleTestItemImpl testClass5 = new SimpleTestItemImpl(5);

    List items = new ArrayList();
    items.add(testClass1);
    items.add(testClass2);
    items.add(testClass3);
    items.add(testClass4);
    items.add(testClass5);
    return items;
  }


  public List<? extends ResultItemType> createListWithNulls()
  {
    List items = new ArrayList();
    items.add(new SimpleTestItemImpl());
    items.add(null);
    items.add(null);
    items.add(new SimpleTestItemImpl());
    return items;
  }


  public void addItem(List items, int compareValue)
  {
    items.add(new SimpleTestItemImpl(compareValue));
  }

}
