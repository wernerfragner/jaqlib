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
public class QueryResult<T, DataSourceType> extends
    QueryItem<T, DataSourceType>
{

  public QueryResult(Query<T, DataSourceType> query)
  {
    super(query);
  }


  public List<T> toList()
  {
    return getQuery().getListResult();
  }


  public Vector<T> toVector()
  {
    return getQuery().getVectorResult();
  }


  public Set<T> toSet()
  {
    return getQuery().getSetResult();
  }


  public <KeyType> Map<KeyType, T> toMap(KeyType key)
  {
    return getQuery().getMapResult(key);
  }


  public <KeyType> Hashtable<KeyType, T> toHashtable(KeyType key)
  {
    return getQuery().getHashtableResult(key);
  }


  public T uniqueResult()
  {
    return getQuery().getUniqueResult();
  }


  public T firstResult()
  {
    return getQuery().getFirstResult();
  }


  public T lastResult()
  {
    return getQuery().getLastResult();
  }


  /**
   * Simple condition that can be used to test all single elements for various
   * conditions.
   * 
   * @param <R> the result element type.
   * @return
   */
  public <R> SingleElementWhereCondition<T, DataSourceType, R> and()
  {
    return getQuery().addSimpleWhereCondition();
  }


  public QueryResult<T, DataSourceType> and(WhereCondition<T> condition)
  {
    return getQuery().addAndWhereCondition(condition);
  }


  public QueryResult<T, DataSourceType> or(WhereCondition<T> condition)
  {
    return getQuery().addOrWhereCondition(condition);
  }


  public <R> ComparableWhereCondition<T, DataSourceType, R> andMethodCallResult(
      R evalResult)
  {
    return getQuery().addReflectiveAndWhereCondition();
  }


  public <R> ComparableWhereCondition<T, DataSourceType, R> orMethodCallResult(
      R evalResult)
  {
    return getQuery().addReflectiveOrWhereCondition();
  }

}
