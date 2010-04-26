package org.jaqlib.core;

/**
 * Abstraction for a data source that is used for a query. The query can use
 * this class to retrieve 'raw' data from the data source.
 * 
 * @author Werner Fragner
 */
public interface SelectDataSource
{

  /**
   * Executes the query on the data source.
   * 
   * @return the 'raw' result of the query. This result normally must be
   *         converted to a user-defined result (e.g. a Java bean).
   */
  DsResultSet execute();

}
