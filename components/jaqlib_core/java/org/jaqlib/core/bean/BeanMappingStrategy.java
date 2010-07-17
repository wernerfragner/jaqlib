package org.jaqlib.core.bean;

import java.util.List;


/**
 * Defines a strategy how to retrieve the mapping between source fields (e.g.
 * database columns, XML attributes, ...) and Java bean fields.
 * 
 * @author Werner Fragner
 */
public interface BeanMappingStrategy
{

  /**
   * Gets the mappings for the given bean class.
   * 
   * @param beanClass the not null Java bean class.
   * @return a list containing all mappings for the given bean class.
   */
  List<FieldMapping<?>> getMappings(Class<?> beanClass);

}
