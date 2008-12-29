package org.jaqlib;

import java.util.Map;

import org.jaqlib.util.CollectionUtil;

/**
 * @author Werner Fragner
 */
public enum CreditRating
{

  GOOD(2), POOR(1);

  private static Map<Integer, CreditRating> ratings;
  private final int intValue;


  private CreditRating(int intValue)
  {
    this.intValue = intValue;
    register(this);
  }


  private static void register(CreditRating rating)
  {
    if (ratings == null)
    {
      ratings = CollectionUtil.newDefaultMap();
    }
    ratings.put(rating.intValue, rating);
  }


  public int intValue()
  {
    return intValue;
  }


  public static CreditRating rating(int intValue)
  {
    return ratings.get(intValue);
  }

}
