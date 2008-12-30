package org.jaqlib.db.java.typehandler;

/**
 * Converts a given value to an other value.
 * 
 * @author Werner Fragner
 */
public interface JavaTypeHandler
{

  /**
   * @param value a null object that may be null.
   * @return the converted or untouched object (depending on the type handler
   *         implementation).
   */
  Object getObject(Object value);

}
