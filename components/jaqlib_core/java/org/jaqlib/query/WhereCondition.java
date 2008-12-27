package org.jaqlib.query;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface WhereCondition<T>
{

  /**
   * Checks if the given item matches this condition.
   * 
   * @param item the item to be checked. <b>This item can be null!</b>
   * @return true if the given item matches this condition; false otherwise.
   */
  boolean evaluate(T item);

}
