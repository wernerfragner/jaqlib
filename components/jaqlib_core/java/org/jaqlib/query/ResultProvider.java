package org.jaqlib.query;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 */
public interface ResultProvider<T, DataSourceType>
{

  List<T> getListResult();


  Vector<T> getVectorResult();


  Set<T> getSetResult();


  <KeyType> Map<KeyType, T> getMapResult(KeyType key);


  <KeyType> Hashtable<KeyType, T> getHashtableResult(KeyType key);


  T getLastResult();


  T getFirstResult();


  T getUniqueResult();

}