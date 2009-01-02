package org.jaqlib.core;

/**
 * @author Werner Fragner
 * 
 * @param <T> the element type.
 */
public interface Task<T>
{

  void execute(T element);

}
