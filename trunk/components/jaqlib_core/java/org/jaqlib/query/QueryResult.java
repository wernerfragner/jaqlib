package org.jaqlib.query;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Provides methods to return the result of the query. It also provides methods
 * to add additional where conditions.
 * 
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


  /**
   * @return a list containing the matching elements. If no matches have been
   *         found then an empty list is returned.
   */
  public List<T> toList()
  {
    return getQuery().getListResult();
  }


  /**
   * @return a vector containing the matching elements. If no matches have been
   *         found then an empty vector is returned.
   */
  public Vector<T> toVector()
  {
    return getQuery().getVectorResult();
  }


  /**
   * @return a set containing the matching elements. If no matches have been
   *         found then an empty set is returned.
   */
  public Set<T> toSet()
  {
    return getQuery().getSetResult();
  }


  /**
   * <p>
   * Creates a map containing the matching elements. The keys for the elements
   * are retrieved by reflection. The method that returns the key of the
   * elements must be called on a 'dummy' object.
   * </p>
   * 
   * <p>
   * <b>Example:</b>
   * 
   * <pre>
   * Account account = QB.getMethodCallRecorder(Account.class);
   * Map&lt;String, Account&gt; results = QB.select(Account.class).from(accounts).toMap(
   *     account.getId());
   * </pre>
   * 
   * </p>
   * 
   * @param key the result of an method invocation on a 'dummy' element.
   * @return a map containing the matching elements. If no matches have been
   *         found then an empty map is returned.
   */
  public <KeyType> Map<KeyType, T> toMap(KeyType key)
  {
    return getQuery().getMapResult(key);
  }


  /**
   * Basically the same as {@link #toMap(Object)}, but this method returns a
   * {@link Hashtable} instead of a {@link Map}.
   * 
   * @return a hashtable containing the matching elements. If no matches have
   *         been found then an empty hashtable is returned.
   */
  public <KeyType> Hashtable<KeyType, T> toHashtable(KeyType key)
  {
    return getQuery().getHashtableResult(key);
  }


  /**
   * @return the unique result of the query. If more than one element matches
   *         the query then an {@link IllegalStateException} is thrown. Returns
   *         null if no match has been found.
   * 
   * @throws IllegalStateException if the query matches more than one element.
   */
  public T uniqueResult()
  {
    return getQuery().getUniqueResult();
  }


  /**
   * @return the first element that matches the query. Returns null if no match
   *         has been found.
   */
  public T firstResult()
  {
    return getQuery().getFirstResult();
  }


  /**
   * @return the last element that matches the query. Returns null if no match
   *         has been found.
   */
  public T lastResult()
  {
    return getQuery().getLastResult();
  }


  /**
   * Simple condition that can be used to test all elements for various
   * conditions.
   * 
   * @param <R> the result element type.
   * @return an object that represents a WHERE condition on a single element of
   *         the source collection.
   */
  public <R> SingleElementWhereCondition<T, DataSourceType, R> and()
  {
    return getQuery().addSimpleWhereCondition();
  }


  /**
   * Adds the given {@link WhereCondition} to the query using a AND connector.
   * 
   * @param condition
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  public QueryResult<T, DataSourceType> and(WhereCondition<T> condition)
  {
    return getQuery().addAndWhereCondition(condition);
  }


  /**
   * Adds the given {@link WhereCondition} to the query using a OR connector.
   * 
   * @param condition
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  public QueryResult<T, DataSourceType> or(WhereCondition<T> condition)
  {
    return getQuery().addOrWhereCondition(condition);
  }


  /**
   * Uses a recorded method call to test all elements for a specific condition.
   * This condition can be specified in the returned
   * {@link ComparableWhereCondition}. The condition is added to the query by
   * using a AND connector.
   * 
   * @param evalResult the result of the recorded method call.
   * @return an object to specify the condition.
   */
  public <R> ComparableWhereCondition<T, DataSourceType, R> andMethodCallResult(
      R evalResult)
  {
    return getQuery().addReflectiveAndWhereCondition();
  }


  /**
   * Uses a recorded method call to test all elements for a specific condition.
   * This condition can be specified in the returned
   * {@link ComparableWhereCondition}. The condition is added to the query by
   * using a OR connector.
   * 
   * @param evalResult the result of the recorded method call.
   * @return an object to specify the condition.
   */
  public <R> ComparableWhereCondition<T, DataSourceType, R> orMethodCallResult(
      R evalResult)
  {
    return getQuery().addReflectiveOrWhereCondition();
  }

}
