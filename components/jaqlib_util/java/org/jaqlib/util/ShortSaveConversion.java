package org.jaqlib.util;

public class ShortSaveConversion implements SaveConversion
{

  public Object convert(Object value, Class<?> targetClass)
  {
    Short givenValue = (Short) value;

    if (targetClass.equals(Short.class) || targetClass.equals(Short.TYPE))
    {
      return givenValue;
    }
    else if (targetClass.equals(Integer.class)
        || targetClass.equals(Integer.TYPE))
    {
      return Integer.valueOf(givenValue);
    }
    else if (targetClass.equals(Long.class) || targetClass.equals(Long.TYPE))
    {
      return Long.valueOf(givenValue);
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
