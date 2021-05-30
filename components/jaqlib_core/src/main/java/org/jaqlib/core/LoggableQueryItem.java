package org.jaqlib.core;

/**
 * This interface marks query items that are able to contribute a log string for
 * building a string representation of the query.
 * 
 * @author Werner Fragner
 */
public interface LoggableQueryItem
{

  /**
   * @param sb the builder to append the log string to.
   */
  void appendLogString(StringBuilder sb);

}
