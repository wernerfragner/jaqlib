package org.jaqlib.util;

import java.util.Map;

/**
 * Performs save conversions (without loss of data) of primitive data types.
 * 
 * @author Werner Fragner
 */
public class SaveConversions
{

  private static Map<Class<?>, SaveConversion> saveConversions = CollectionUtil
      .newDefaultMap();

  static
  {
    saveConversions.put(Short.class, new ShortSaveConversion());
    saveConversions.put(Short.TYPE, new ShortSaveConversion());
    saveConversions.put(Integer.class, new IntegerSaveConversion());
    saveConversions.put(Integer.TYPE, new IntegerSaveConversion());
    saveConversions.put(Long.class, new LongSaveConversion());
    saveConversions.put(Long.TYPE, new LongSaveConversion());
    saveConversions.put(Float.class, new FloatSaveConversion());
    saveConversions.put(Float.TYPE, new FloatSaveConversion());
    saveConversions.put(Character.class, new CharacterSaveConversion());
    saveConversions.put(Character.TYPE, new CharacterSaveConversion());
  }


  public static Object convert(Object value, Class<?> targetType)
  {
    SaveConversion conversion = saveConversions.get(value.getClass());
    if (conversion != null)
    {
      return conversion.convert(value, targetType);
    }
    else
    {
      return value;
    }
  }


}
