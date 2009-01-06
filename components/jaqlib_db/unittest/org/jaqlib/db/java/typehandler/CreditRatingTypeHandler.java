package org.jaqlib.db.java.typehandler;

import org.jaqlib.CreditRating;

/**
 * @author Werner Fragner
 */
public class CreditRatingTypeHandler extends AbstractJavaTypeHandler
{

  public Class<?>[] getSupportedTypes()
  {
    return new Class[] { CreditRating.class };
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
