package org.jaqlib.db.java.typehandler;

/**
 * Converts a given value to an other value.
 * 
 * @author Werner Fragner
 */
public interface BeanFieldTypeHandler
{

  /**
   * @param value a null object that may be null.
   * @return the converted or untouched object (depending on the type handler
   *         implementation).
   */
  Object getValue(Object value);

}
