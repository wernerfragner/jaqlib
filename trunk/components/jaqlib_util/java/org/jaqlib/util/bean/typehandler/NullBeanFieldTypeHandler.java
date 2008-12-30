package org.jaqlib.util.bean.typehandler;

/**
 * @author Werner Fragner
 */
public class NullBeanFieldTypeHandler implements BeanFieldTypeHandler
{

  public Object getValue(Object value)
  {
    return value;
  }

}
