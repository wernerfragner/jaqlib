package org.jaqlib.db.java.typehandler;

/**
 * @author Werner Fragner
 */
public class NullJavaTypeHandler implements JavaTypeHandler
{

  public Object getObject(Object value)
  {
    return value;
  }

}
