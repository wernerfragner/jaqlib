package org.jaqlib.util;

public class LongSaveConversion implements SaveConversion
{

  public Object convert(Object value, Class<?> targetClass)
  {
    Long givenValue = (Long) value;

    if (targetClass.equals(Long.class) || targetClass.equals(Long.TYPE))
    {
      return givenValue;
    }
    else if (targetClass.equals(Float.class) || targetClass.equals(Float.TYPE))
    {
      return Float.valueOf(givenValue);
    }
    else if (targetClass.equals(Double.class)
        || targetClass.equals(Double.TYPE))
    {
      return Double.valueOf(givenValue);
    }
    else
    {
      return value;
    }
  }
}
