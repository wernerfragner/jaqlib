package org.jaqlib;


/**
 * @author Werner Fragner
 */
public class ReflectiveConditionClassTest extends
    AbstractReflectiveConditionTest<SimpleTestElementImpl>
{

  @Override
  protected Class<SimpleTestElementImpl> getResultElementClass()
  {
    return SimpleTestElementImpl.class;
  }


  @Override
  protected SimpleTestElementImpl createElement(int compareValue)
  {
    return new SimpleTestElementImpl(compareValue);
  }


}
