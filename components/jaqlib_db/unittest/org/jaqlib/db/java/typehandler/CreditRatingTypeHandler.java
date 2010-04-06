package org.jaqlib.db.java.typehandler;

import java.util.List;

import org.jaqlib.CreditRating;
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
      throw handleIllegalInputValue(value, CreditRating.class);
    }
  }

}
