package org.jaqlib.db.java.typehandler;


public class BooleanTypeHandler extends AbstractJavaTypeHandler
{

  public Object convert(Object value)
  {
    if (value instanceof Integer)
    {
      Integer iValue = (Integer) value;
      return iValue == 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    else if (value instanceof Boolean)
    {
      Boolean bValue = (Boolean) value;
      return bValue ? Integer.valueOf(0) : Integer.valueOf(1);
    }
    else
    {
      throw handleIllegalInputValue(value, Boolean.class);
    }
  }

}
