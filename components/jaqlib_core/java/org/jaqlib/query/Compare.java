package org.jaqlib.query;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface Compare<T>
{

  boolean evaluate(T item);

}
