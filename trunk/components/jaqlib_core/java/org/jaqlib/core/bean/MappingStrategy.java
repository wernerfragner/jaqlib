package org.jaqlib.core.bean;

import java.util.List;


/**
 * Defines a strategy how to retrieve the mapping between database columns and
 * Java bean instance fields.
 * 
 * @author Werner Fragner
 */
public interface MappingStrategy
{

  /**
   * Gets the mappings for the given bean class.
   * 
   * @param beanClass the not null Java bean class.
   * @return a list containing all mappings for the given bean class.
   */
  List<FieldMapping<?>> getMappings(Class<?> beanClass);

}
