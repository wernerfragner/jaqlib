package org.jaqlib.query;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSoureType>
 */
public interface QueryBuilder<T, DataSoureType>
{

  Query<T, DataSoureType> createQuery();

}
