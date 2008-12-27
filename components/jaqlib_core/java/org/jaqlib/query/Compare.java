package org.jaqlib.query;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface Compare<T, ResultType>
{

  boolean evaluate(T element);

}
