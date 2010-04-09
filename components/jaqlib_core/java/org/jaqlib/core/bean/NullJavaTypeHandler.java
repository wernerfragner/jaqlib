package org.jaqlib.core.bean;


/**
 * @author Werner Fragner
 */
public class NullJavaTypeHandler implements JavaTypeHandler
{

  public Object convert(Object value)
  {
    return value;
  }


  public Class<?>[] getSupportedTypes()
  {
    return new Class[] { Object.class };
  }

}
