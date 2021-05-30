package org.jaqlib.core;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Provides methods to return the result of the query. It also provides methods
 * to add additional WHERE conditions.
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
   * @return a list containing all matching elements. If no matches have been
   *         found then an empty list is returned.
   */
  public List<T> asList()
  {
    return getQuery().getListResult();
  }


  /**
   * @return a vector containing all matching elements. If no matches have been
   *         found then an empty vector is returned.
   */
  public Vector<T> asVector()
  {
    return getQuery().getVectorResult();
  }


  /**
   * @return a set containing all matching elements. If no matches have been
   *         found then an empty set is returned.
   */
  public Set<T> asSet()
  {
    return getQuery().getSetResult();
  }


  /**
   * <p>
   * Creates a map containing all matching elements. The keys for the elements
   * are retrieved by using the method call recording mechanism. The method that
   * returns the desired key must be called on a proxy object. This method call
   * is recorded by JaQLib and is replayed for every element in the result set.
   * </p>
   * 
   * <p>
   * <b>Example:</b>
   * 
   * <pre>
   * Account account = IterableQB.getRecorder(Account.class);
   * Map&lt;Long, Account&gt; results = IterableQB.select(Account.class).from(accounts)
   *     .asMap(account.getId());
   * </pre>
   * 
   * </p>
   * 
   * @param key
   *          the result of the recorded method call. This result is only needed
   *          for type safety. The object itself is not used.
   * @return a map containing all matching elements. If no matches have been
   *         found then an empty map is returned.
   */
  public <KeyType> Map<KeyType, T> asMap(KeyType key)
  {
    return getQuery().getMapResult(key);
  }


  /**
   * Basically the same as {@link #asMap(Object)}, but this method returns a
   * {@link Hashtable} instead of a {@link Map}.
   * 
   * @return a hashtable containing all matching elements. If no matches have
   *         been found then an empty hashtable is returned.
   */
  public <KeyType> Hashtable<KeyType, T> asHashtable(KeyType key)
  {
    return getQuery().getHashtableResult(key);
  }


  /**
   * @return the unique result of the query. If more than one element matches
   *         the query then an {@link QueryResultException} is thrown. Returns
   *         null if no match has been found.
   * 
   * @throws QueryResultException
   *           if the query matches more than one element.
   */
  public T asUniqueResult()
  {
    return getQuery().getUniqueResult();
  }


  /**
   * @return the first element that matches the query. Returns null if no match
   *         has been found.
   */
  public T asFirstResult()
  {
    return getQuery().getFirstResult();
  }


  /**
   * @return the last element that matches the query. Returns null if no match
   *         has been found.
   */
  public T asLastResult()
  {
    return getQuery().getLastResult();
  }


  /**
   * This method is an alias for {@link #asUniqueResult()}.
   */
  public T uniqueResult()
  {
    return asUniqueResult();
  }


  /**
   * This method is an alias for {@link #asFirstResult()}.
   */
  public T firstResult()
  {
    return asFirstResult();
  }


  /**
   * This method is an alias for {@link #asLastResult()}.
   */
  public T lastResult()
  {
    return asLastResult();
  }


  /**
   * Executes the given task for each element in the data source.
   * <p>
   * <b>The task is executed when the result of the query is requested (using
   * {@link #asList()}, {@link #uniqueResult()}, ...). If no result is needed
   * then {@link #execute(Task)} should be used.</b>
   * </p>
   * 
   * @param task
   *          the not null task to be executed.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  public QueryResult<T, DataSourceType> executeWithResult(Task<? super T> task)
  {
    return getQuery().addTask(task);
  }


  /**
   * Executes the given task for each element in the data source immediately.
   * 
   * @param task
   *          the not null task to be executed.
   */
  public void execute(Task<? super T> task)
  {
    getQuery().addTaskAndExecute(task);
  }


  /**
   * Simple condition that can be used to test all elements for a specific
   * condition. This condition is appended using an AND connector.
   * 
   * @param <ResultType>
   *          the result element type.
   * @return an object that represents a WHERE condition on a single element of
   *         the data source.
   */
  public <ResultType> SingleElementWhereCondition<T, DataSourceType, ResultType> and()
  {
    return getQuery().addElementAndWhereCondition();
  }


  /**
   * Shortcut method for <tt>and().element()</tt>.
   * 
   * @param <ResultType>
   *          the result element type.
   * @return an object to specify the condition on an element.
   */
  public <ResultType> ComparableWhereCondition<T, DataSourceType, ResultType> andElement()
  {
    return this.<ResultType> and().element();
  }


  /**
   * Simple condition that can be used to test all elements for a specific
   * condition. This condition is appended using an OR connector.
   * 
   * @param <R>
   *          the result element type.
   * @return an object that represents a WHERE condition on a single element of
   *         the data source.
   */
  public <R> SingleElementWhereCondition<T, DataSourceType, R> or()
  {
    return getQuery().addElementOrWhereCondition();
  }


  /**
   * Shortcut method for <tt>or().element()</tt>.
   * 
   * @param <ResultType>
   *          the result element type.
   * @return an object to specify the condition on an element.
   */
  public <ResultType> ComparableWhereCondition<T, DataSourceType, ResultType> orElement()
  {
    return this.<ResultType> or().element();
  }


  /**
   * Adds the given {@link WhereCondition} to the query using a AND connector.
   * 
   * @param condition
   *          a custom WHERE condition.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  public QueryResult<T, DataSourceType> and(WhereCondition<? super T> condition)
  {
    return getQuery().addAndWhereCondition(condition);
  }


  /**
   * Adds the given {@link WhereCondition} to the query using a OR connector.
   * 
   * @param condition
   *          a custom WHERE condition.
   * @return an object to retrieve the result of the query or to add more WHERE
   *         conditions to the query.
   */
  public QueryResult<T, DataSourceType> or(WhereCondition<? super T> condition)
  {
    return getQuery().addOrWhereCondition(condition);
  }


  /**
   * Uses a recorded method call to test all elements for a specific condition.
   * This condition can be specified in the returned
   * {@link ComparableWhereCondition}. The condition is added to the query by
   * using a AND connector.
   * 
   * @param evalResult
   *          the result of the recorded method call. This result is only needed
   *          for type safety. The object itself is not used.
   * @return an object to specify the condition.
   */
  public <R> ComparableWhereCondition<T, DataSourceType, R> andCall(R evalResult)
  {
    return getQuery().addReflectiveAndWhereCondition();
  }


  /**
   * Uses a recorded method call to test all elements for a specific condition.
   * This condition can be specified in the returned
   * {@link ComparableWhereCondition}. The condition is added to the query by
   * using a OR connector.
   * 
   * @param evalResult
   *          the result of the recorded method call. This result is only needed
   *          for type safety. The object itself is not used.
   * @return an object to specify the condition.
   */
  public <R> ComparableWhereCondition<T, DataSourceType, R> orCall(R evalResult)
  {
    return getQuery().addReflectiveOrWhereCondition();
  }


  /**
   * Uses a recored method call to test all elements for a boolean condition.
   * The condition must return true in order to add the element to the result
   * set.
   * 
   * @param evalResult
   *          the result of the recorded method call. This result is only needed
   *          for type safety. The object itself is not used.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public <R> QueryResult<T, DataSourceType> orCallIsTrue(Boolean evalResult)
  {
    return getQuery().addReflectiveOrWhereCondition().isEqual(true);
  }


  /**
   * Uses a recored method call to test all elements for a boolean condition.
   * The condition must return false in order to add the element to the result
   * set.
   * 
   * @param evalResult
   *          the result of the recorded method call. This result is only needed
   *          for type safety. The object itself is not used.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public <R> QueryResult<T, DataSourceType> orCallIsFalse(Boolean evalResult)
  {
    return getQuery().addReflectiveOrWhereCondition().isEqual(false);
  }


  /**
   * Uses a recored method call to test all elements for a boolean condition.
   * The condition must return true in order to add the element to the result
   * set.
   * 
   * @param evalResult
   *          the result of the recorded method call. This result is only needed
   *          for type safety. The object itself is not used.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public <R> QueryResult<T, DataSourceType> andCallIsTrue(Boolean evalResult)
  {
    return getQuery().addReflectiveAndWhereCondition().isEqual(true);
  }


  /**
   * Uses a recored method call to test all elements for a boolean condition.
   * The condition must return false in order to add the element to the result
   * set.
   * 
   * @param evalResult
   *          the result of the recorded method call. This result is only needed
   *          for type safety. The object itself is not used.
   * @return the result of the query (including methods to add other WHERE
   *         conditions).
   */
  public <R> QueryResult<T, DataSourceType> andCallIsFalse(Boolean evalResult)
  {
    return getQuery().addReflectiveAndWhereCondition().isEqual(false);
  }


  /**
   * @return the number of elements in the result set. Null elements in the
   *         result set are also counted.
   */
  public int count()
  {
    return getQuery().count();
  }


  /**
   * @return the number of distinct elements in the result set. The equals
   *         method of the elements are used to determine the distinct elements.
   *         A possible null element in the result set is also counted.
   */
  public int countDistinct()
  {
    return getQuery().countDistinct();
  }

}
