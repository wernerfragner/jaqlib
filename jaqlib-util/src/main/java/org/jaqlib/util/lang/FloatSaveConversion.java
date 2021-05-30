package org.jaqlib.util.lang;

public class FloatSaveConversion implements SaveConversion
{

  public Object convert(Object value, Class<?> targetClass)
  {
    Float givenValue = (Float) value;

    if (targetClass.equals(Float.class) || targetClass.equals(Float.TYPE))
    {
      return givenValue;
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
