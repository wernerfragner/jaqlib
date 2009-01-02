package org.jaqlib.core;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface Compare<T, ResultType> extends LoggableQueryItem
{

  boolean evaluate(T element);

}
