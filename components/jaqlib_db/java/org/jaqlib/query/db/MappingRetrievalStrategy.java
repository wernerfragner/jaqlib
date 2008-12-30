package org.jaqlib.query.db;


/**
 * Defines a strategy how to retrieve the mapping between database columns and
 * Java bean instance fields.
 * 
 * @author Werner Fragner
 */
public interface MappingRetrievalStrategy
{

  /**
   * Fills the given result object with mappings.
   * 
   * @param beanClass the not null Java bean class.
   * @param result a not null result where to store the mappings.
   */
  void addMappings(Class<?> beanClass, BeanMapping<?> result);

}
