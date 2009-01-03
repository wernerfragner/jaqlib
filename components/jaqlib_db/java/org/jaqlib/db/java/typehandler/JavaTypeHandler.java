package org.jaqlib.db.java.typehandler;

/**
 * Converts a given database value to a Java type value.
 * 
 * @author Werner Fragner
 */
public interface JavaTypeHandler
{

  /**
   * @param value a null object that may be null.
   * @return the converted or untouched object (depending on the type handler
   *         implementation).
   * @throws IllegalArgumentException if the given value cannot be converted to
   *           the desired Java type.
   */
  Object getObject(Object value);

}
