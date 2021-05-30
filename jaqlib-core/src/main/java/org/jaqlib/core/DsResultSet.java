package org.jaqlib.core;

import org.jaqlib.core.bean.FieldMapping;

/**
 * Represent the result set of a data source.
 * 
 * @author Werner Fragner
 */
public interface DsResultSet
{

  /**
   * Object that indicates that no object has been found for a given
   * {@link FieldMapping}.
   */
  Object NO_RESULT = new Object();


  /**
   * While this method returns true the method {@link #getObject(FieldMapping)}
   * resp. {@link #getAnynomousObject(FieldMapping)} returns an object.
   * 
   * @return true if another object is available; false otherwise.
   */
  boolean next();


  /**
   * Gets the object for the given field.
   * 
   * @param mapping the definition describing the object.
   * @return see description.
   */
  Object getObject(FieldMapping<?> mapping);


  /**
   * Gets an anonymous object for the given field. The name of the given field
   * is ignored, only the type and the type handler are used. This method can be
   * used if the bean is no Java object but a primitive object (e.g., select
   * count(*) as cnt from ...).
   * 
   * @param mapping the mapping to use.
   * @return the object for the given mapping.
   */
  Object getAnynomousObject(FieldMapping<?> mapping);


}
