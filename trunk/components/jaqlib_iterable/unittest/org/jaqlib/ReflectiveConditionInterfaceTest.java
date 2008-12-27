package org.jaqlib;


/**
 * @author Werner Fragner
 */
public class ReflectiveConditionInterfaceTest extends
    AbstractReflectiveConditionTest<SimpleTestElement>
{

  @Override
  protected Class<SimpleTestElement> getResultElementClass()
  {
    return SimpleTestElement.class;
  }


  @Override
  protected SimpleTestElement createElement(int compareValue)
  {
    return new SimpleTestElementImpl(compareValue);
  }


}
