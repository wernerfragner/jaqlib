package org.jaqlib.xml;

import java.util.List;

import org.jaqlib.CreditRating;
import org.jaqlib.core.bean.AbstractJavaTypeHandler;

public class CreditRatingStringTypeHandler extends AbstractJavaTypeHandler
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

      // return value;
      throw super.handleIllegalInputValue(value, CreditRating.class);
    }

    return value;
  }

}
