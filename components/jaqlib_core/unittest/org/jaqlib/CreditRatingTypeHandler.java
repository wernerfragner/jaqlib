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
      value = Integer.valueOf((String) value);
    }
    if (value instanceof Integer)
    {
      return CreditRating.rating((Integer) value);
    }
    else if (value instanceof CreditRating)
    {
      CreditRating rating = (CreditRating) value;
      return rating.intValue();
    }
    else
    {
      // return value;
      throw handleIllegalInputValue(value, CreditRating.class);
    }
  }

}
