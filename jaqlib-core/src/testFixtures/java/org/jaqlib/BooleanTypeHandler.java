package org.jaqlib;

import org.jaqlib.core.bean.AbstractJavaTypeHandler;

import java.util.List;

/**
 * @author Werner Fragner
 */
public class BooleanTypeHandler extends AbstractJavaTypeHandler
{

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSupportedTypes(List<Class<?>> types)
  {
    types.add(Boolean.class);
    types.add(Boolean.TYPE);
  }


  /**
   * {@inheritDoc}
   */
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
