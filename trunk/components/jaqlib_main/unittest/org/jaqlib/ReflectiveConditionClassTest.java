package org.jaqlib;


/**
 * @author Werner Fragner
 */
public class ReflectiveConditionClassTest extends
    AbstractReflectiveConditionTest<SimpleTestItemImpl>
{

  @Override
  protected Class<SimpleTestItemImpl> getResultItemClass()
  {
    return SimpleTestItemImpl.class;
  }


  @Override
  protected SimpleTestItemImpl createItem(int compareValue)
  {
    return new SimpleTestItemImpl(compareValue);
  }


}
