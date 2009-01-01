package org.jaqlib.db;


/**
 * Fetch strategy that stops at the first occurrence of a matching element.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the element type of the query.
 */
public class SimpleFetchStrategy<T> extends AbstractFetchStrategy<T>
{

  /**
   * If a match has been found then the fetch operation can be stopped.
   */
  @Override
  protected boolean recordProcessed(T element, boolean isMatch)
  {
    return isMatch;
  }

}
