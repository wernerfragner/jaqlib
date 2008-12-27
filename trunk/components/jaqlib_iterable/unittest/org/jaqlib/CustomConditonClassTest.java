package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class CustomConditonClassTest extends
    AbstractCustomConditonTest<SimpleTestElementImpl>
{

  @Override
  protected Class<SimpleTestElementImpl> getResultElementClass()
  {
    return SimpleTestElementImpl.class;
  }


}
