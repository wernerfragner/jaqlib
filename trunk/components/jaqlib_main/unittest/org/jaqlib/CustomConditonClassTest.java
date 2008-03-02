package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class CustomConditonClassTest extends
    AbstractCustomConditonTest<SimpleTestItemImpl>
{

  @Override
  protected Class<SimpleTestItemImpl> getResultItemClass()
  {
    return SimpleTestItemImpl.class;
  }


}
