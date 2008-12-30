package org.jaqlib.db.java.typehandler;

import org.jaqlib.CreditRating;
import org.jaqlib.db.java.typehandler.AbstractJavaTypeHandler;

/**
 * @author Werner Fragner
 */
public class CreditRatingTypeHandler extends AbstractJavaTypeHandler
{

  public Object getObject(Object value)
  {
    if (value instanceof Integer)
    {
      return CreditRating.rating((Integer) value);
    }
    else
    {
      throw handleIllegalInputValue(value, CreditRating.class);
    }
  }

}
