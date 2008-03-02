package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class CustomConditonInterfaceTest extends
    AbstractCustomConditonTest<SimpleTestItem>
{

  @Override
  protected Class<SimpleTestItem> getResultItemClass()
  {
    return SimpleTestItem.class;
  }


}
