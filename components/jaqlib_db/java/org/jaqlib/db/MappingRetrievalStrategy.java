package org.jaqlib.db;


/**
 * Defines a strategy how to retrieve the mapping between database columns and
 * java bean instance fields.
 * 
 * @author Werner Fragner
 */
public interface MappingRetrievalStrategy
{

  /**
   * Fills the given result object with mappings.
   * 
   * @param result a not null result where to store the mappings.
   */
  void addMappings(BeanDbSelectResult<?> result);

}
