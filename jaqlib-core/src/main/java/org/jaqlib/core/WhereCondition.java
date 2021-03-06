package org.jaqlib.core;

/**
 * Classes that implement this interface can contain custom WHERE condition
 * code.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface WhereCondition<T>
{

  /**
   * Checks if the given element matches this condition.
   * 
   * @param element the element to be checked. <b>This element can be null!</b>
   * @return true if the given element matches this condition; false otherwise.
   */
  boolean evaluate(T element);

}
