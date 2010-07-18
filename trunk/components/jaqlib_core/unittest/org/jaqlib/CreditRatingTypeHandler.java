package org.jaqlib;

import java.util.List;

import org.jaqlib.core.bean.AbstractJavaTypeHandler;

/**
 * @author Werner Fragner
 */
public class CreditRatingTypeHandler extends AbstractJavaTypeHandler
{

  @Override
  public void addSupportedTypes(List<Class<?>> types)
  {
    types.add(CreditRating.class);
  }


  public Object convert(Object value)
  {
    if (value instanceof String)
    {
      String str = (String) value;
      if (str.equals(CreditRating.POOR.getName()))
      {
        return CreditRating.POOR;
      }
      else if (str.equals(CreditRating.GOOD.getName()))
      {
        return CreditRating.GOOD;
      }
    }

    throw handleIllegalInputValue(value, CreditRating.class);
  }

}
