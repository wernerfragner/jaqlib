package org.jaqlib;


/**
 * @author Werner Fragner
 */
public class ReflectiveConditionInterfaceTest extends
    AbstractReflectiveConditionTest<SimpleTestItem>
{

  @Override
  protected Class<SimpleTestItem> getResultItemClass()
  {
    return SimpleTestItem.class;
  }


  @Override
  protected SimpleTestItem createItem(int compareValue)
  {
    return new SimpleTestItemImpl(compareValue);
  }


}
