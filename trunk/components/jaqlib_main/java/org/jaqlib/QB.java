/*
 * Copyright 2008 Werner Fragner
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.jaqlib;

import org.jaqlib.query.FromClause;
import org.jaqlib.query.Query;
import org.jaqlib.query.QueryBuilder;
import org.jaqlib.query.QueryBuilderFactory;
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.query.iterable.IterableQueryBuilderFactory;
import org.jaqlib.reflect.JaqlibInvocationRecorder;
import org.jaqlib.reflect.JaqlibProxy;
import org.jaqlib.util.Assert;


/**
 * QB ... QueryBuilder
 * <p>
 * Main entry point of JaQLib. It provides methods for building queries (
 * {@link #select(Class)}, {@link #select(Class...)}) and adapting the query
 * building process ({@link #setClassLoader(ClassLoader)},
 * {@link #setQueryBuilderFactory(QueryBuilderFactory)}). The method
 * {@link #getMethodCallRecorder(Class)} can be used to define a WHERE condition
 * by calling a method on a 'dummy' collection item.
 * </p>
 * <p>
 * <b>Usage examples:</b><br>
 * <i>Example with method call recording:</i>
 * 
 * <pre>
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = QB.getMethodCallRecorder(Account.class);
 * 
 * // select all accounts with a balance that is greater than 5000
 * List&lt;Account&gt; result = QB.select(Account.class).from(accounts).where(
 *     account.getBalance()).isGreaterThan(5000).toList();
 * </pre>
 * 
 * <i>Example with user-defined WHERE conditions:</i>
 * 
 * <pre>
 * // create condition for negative balances
 * WhereCondition deptCondition = new WhereCondition() {
 * 
 *   public boolean evaluate(Account account) {
 *     return (account.getBalance() &lt; 0);
 *   }
 * 
 * };
 * 
 * // create condition for accounts with poor credit rating
 * WhereCondition ratingCondition = new WhereCondition() {
 * 
 *   public boolean evaluate(Account account) {
 *     return (account.getCreditRating() == CreditRating.POOR);
 *   }
 * }
 * 
 * // execute query with these conditions 
 * List&lt;Account&gt; result = QB.select(Account.class).from(accounts).where(deptCondition).and(ratingCondition).toList();
 * </pre>
 * 
 * <i>Example for filtering out null items:</i>
 * 
 * <pre>
 * List&lt;Account&gt; notNullAccounts = QB.select(Account.class).from(accounts).where()
 *     .item().isNotNull().toList();
 * </pre>
 * 
 * <i>Example for using {@link Comparable} items:</i>
 * 
 * <pre>
 * Account spec = new Account();
 * account.setBalance(5000);
 * 
 * List&lt;Account&gt; result = QB.select(Account.class).from(accounts).where().item()
 *     .isSmallerThan(spec).toList();
 * </pre>
 * 
 * </p>
 * 
 * @author Werner Fragner
 */
public class QB
{

  protected static final ThreadLocal<JaqlibInvocationRecorder> invocationRecorder = new ThreadLocal<JaqlibInvocationRecorder>();

  protected static final ThreadLocal<ClassLoader> classLoader = new ThreadLocal<ClassLoader>();
  protected static final ThreadLocal<QueryBuilderFactory> queryBuilderFactory = new ThreadLocal<QueryBuilderFactory>();


  /**
   * Initializes this class with the a default class loader and a default
   * {@link QueryBuilderFactory}.
   */
  static
  {
    // set default class loader
    classLoader.set(QB.class.getClassLoader());
    // set default query builder factory
    queryBuilderFactory.set(new IterableQueryBuilderFactory());
  }


  /**
   * Sets a user-defined classloader that is used when creating proxy classes.
   * 
   * @param classLoader
   */
  public static void setClassLoader(ClassLoader classLoader)
  {
    Assert.notNull(classLoader);
    QB.classLoader.set(classLoader);
  }


  /**
   * Sets a user-defined query builder factory. By default the
   * {@link org.jaqlib.query.iterable.IterableQueryBuilderFactory} is used by
   * this class.
   * 
   * @param queryBuilderFactory
   */
  public static void setQueryBuilderFactory(
      QueryBuilderFactory queryBuilderFactory)
  {
    Assert.notNull(queryBuilderFactory);
    QB.queryBuilderFactory.set(queryBuilderFactory);
  }


  /**
   * @param <T>
   * @param resultItemClass
   * @return a proxy object that records all method invocations. These
   *         invocations can be used when evaluating the WHERE clause of query.
   */
  public static <T> T getMethodCallRecorder(Class<T> resultItemClass)
  {
    JaqlibProxy<T> proxy = new JaqlibProxy<T>(classLoader.get());
    invocationRecorder.set(proxy.getInvocationRecorder());
    return proxy.getProxy(resultItemClass);
  }


  /**
   * Selects the a certain set of objects in a given collection. Which
   * collection to use must be specified in the returned {@link FromClause}. The
   * {@link FromClause} hereby return a {@link WhereClause} with which an
   * arbitrary WHERE condition can be specified. This WHERE condition supports
   * AND, OR, the evaluation of a user-defined {@link WhereCondition} and a
   * user-defined {@link ReflectiveWhereCondition}.
   * 
   * @param <T> the collection item type.
   * @param <DataSourceType> the collection type.
   * @param resultItemClass the class of the result items. This class is only
   *          necessary for type safety.
   * @return the FROM clause to specify the source of the query.
   */
  public static <T, DataSourceType> FromClause<T, DataSourceType> select(
      Class<T> resultItemClass)
  {
    QueryBuilder<T, DataSourceType> queryBuilder = getQueryBuilder();
    Query<T, DataSourceType> query = queryBuilder.createQuery();
    return query.createFromClause(resultItemClass);
  }


  /**
   * This method has basically the same functionality as {@link #select(Class)}.
   * But this method supports multiple result item types. That means that the
   * query can also return different result types.
   * 
   * @param <T> the collection item type.
   * @param <DataSourceType> the collection type.
   * @param resultItemClasses the classes of the result items. This class is
   *          only necessary for type safety.
   * @return the FROM clause to specify the source of the query.
   */
  public static <T, DataSourceType> FromClause<T, DataSourceType> select(
      Class<T>... resultItemClasses)
  {
    QueryBuilder<T, DataSourceType> queryBuilder = getQueryBuilder();
    Query<T, DataSourceType> query = queryBuilder.createQuery();
    return query.createFromClause(resultItemClasses);
  }


  /**
   * @param <T> the collection item type.
   * @param <DataSourceType> the collection type.
   * @return the {@link QueryBuilder} for the current thread.
   */
  protected static <T, DataSourceType> QueryBuilder<T, DataSourceType> getQueryBuilder()
  {
    return getQueryBuilderFactory().getQueryBuilder(invocationRecorder.get());
  }


  /**
   * @return the factory for building queries.
   */
  protected static QueryBuilderFactory getQueryBuilderFactory()
  {
    return queryBuilderFactory.get();
  }


}
