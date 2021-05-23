package org.jaqlib.util.lang;

public class CharacterSaveConversion implements SaveConversion
{

  public Object convert(Object value, Class<?> targetType)
  {
    Character givenValue = (Character) value;
    if (targetType.equals(Character.class) || targetType.equals(Character.TYPE))
    {
      return givenValue;
    }
    else if (targetType.equals(String.class))
    {
      return String.valueOf(givenValue);
    }
    else
    {
      return value;
    }
  }

}
