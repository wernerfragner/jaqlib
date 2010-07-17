package org.jaqlib.core.bean;

import org.jaqlib.core.DsResultSet;

/**
 * @author Werner Fragner
 */
public abstract class AbstractMapping<T>
{

  /**
   * Extracts the value that is defined by this class from the given result set.
   * 
   * @param rs the database result set.
   * @return the value from the result set.
   */
  public abstract T getValue(DsResultSet rs);


  /**
   * @return a printable log string describing this mapping.
   */
  public abstract String getLogString();

}
