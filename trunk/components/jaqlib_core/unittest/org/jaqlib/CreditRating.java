package org.jaqlib;

import java.util.Map;

import org.jaqlib.util.CollectionUtil;

/**
 * @author Werner Fragner
 */
public enum CreditRating
{

  GOOD(2, "GOOD"), POOR(1, "POOR");

  private static Map<Integer, CreditRating> ratings;
  private final int intValue;
  private final String name;


  private CreditRating(int intValue, String name)
  {
    this.intValue = intValue;
    this.name = name;
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


  public String getName()
  {
    return name;
  }

}
