package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class CustomConditonInterfaceTest extends
    AbstractCustomConditonTest<SimpleTestElement>
{

  @Override
  protected Class<SimpleTestElement> getResultElementClass()
  {
    return SimpleTestElement.class;
  }


}
