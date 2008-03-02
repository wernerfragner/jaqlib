package org.jaqlib.query;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface WhereCondition<T>
{

  boolean evaluate(T item);

}
