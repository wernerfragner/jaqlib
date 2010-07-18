package org.jaqlib;

import java.util.List;

import org.jaqlib.core.bean.AbstractJavaTypeHandler;

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
    if (value instanceof Boolean)
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
