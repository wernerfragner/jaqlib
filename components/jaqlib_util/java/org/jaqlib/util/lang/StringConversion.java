package org.jaqlib.util.lang;

/**
 * Helper class for converting strings to primitive objects.
 * 
 * @author Werner Fragner
 */
public class StringConversion
{

  /**
   * Converts the given input value to the given target type.
   * 
   * @param targetType
   * @param inputValue
   * @return the converted value.
   */
  public static Object fromString(Class<?> targetType, String inputValue)
  {
    if (inputValue == null)
    {
      return null;
    }
    if (targetType.equals(String.class))
    {
      return inputValue;
    }
    else if (targetType.equals(Integer.class) || targetType == Integer.TYPE)
    {
      return Integer.valueOf(inputValue);
    }
    else if (targetType.equals(Long.class) || targetType == Long.TYPE)
    {
      return Long.valueOf(inputValue);
    }
    else if (targetType.equals(Double.class) || targetType == Double.TYPE)
    {
      return Double.valueOf(inputValue);
    }
    else if (targetType.equals(Float.class) || targetType == Float.TYPE)
    {
      return Float.valueOf(inputValue);
    }
    else if (targetType.equals(Byte.class) || targetType == Byte.TYPE)
    {
      return Byte.valueOf(inputValue);
    }
    else if (targetType.equals(char.class))
    {
      return new Character(inputValue.charAt(0));
    }
    else if (targetType.equals(Boolean.class) || targetType == Boolean.TYPE)
    {
      return Boolean.valueOf(inputValue);
    }

    throw new IllegalArgumentException("Unsupported field type '" + targetType
        + "' for conversion from String.");
  }

}
