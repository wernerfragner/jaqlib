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
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.query.iterable.IterableQueryBuilder;
import org.jaqlib.reflect.MethodCallRecorder;
import org.jaqlib.reflect.RecordingProxy;
import org.jaqlib.util.Assert;


/**
 * <p>
 * The main entry point of JaQLib for {@link Iterable} support. QB stands for
 * QueryBuilder. It provides methods for building queries (
 * {@link #select(Class)}, {@link #select(Class...)}) and adapting the query
 * building process ( {@link #setClassLoader(ClassLoader)}.
 * {@link #getMethodCallRecorder(Class)} can be used to define a WHERE condition
 * by calling a method on a 'dummy' collection element.
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
 * List&lt;Account&gt; highRiskAccounts = QB.select(Account.class).from(accounts)
 *     .where(deptCondition).and(ratingCondition).toList();
 * </pre>
 * 
 * <i>Example for filtering out null elements:</i>
 * 
 * <pre>
 * List&lt;Account&gt; notNullAccounts = QB.select(Account.class).from(accounts).where()
 *     .element().isNotNull().toList();
 * </pre>
 * 
 * <i>Example for using {@link Comparable} elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used for comparing two accounts
 * Account spec = new Account();
 * account.setBalance(5000);
 * 
 * List&lt;Account&gt; result = QB.select(Account.class).from(accounts).where()
 *     .element().isSmallerThan(spec).toList();
 * </pre>
 * 
 * </p>
 * 
 * @author Werner Fragner
 */
public class QB
{

  private static final ThreadLocal<MethodCallRecorder> methodCallRecorder = new ThreadLocal<MethodCallRecorder>();
  private static final ThreadLocal<ClassLoader> classLoader = new ThreadLocal<ClassLoader>();


  /**
   * Initializes this class with the a default class loader.
   */
  static
  {
    // set default class loader
    classLoader.set(QB.class.getClassLoader());
  }


  /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getMethodCallRecorder(Class)} method.
   * 
   * @param classLoader a not null class loader.
   */
  public static void setClassLoader(ClassLoader classLoader)
  {
    Assert.notNull(classLoader);
    QB.classLoader.set(classLoader);
  }


  /**
   * @param <T> the type of the result element(s).
   * @param resultElementClass a not null class of the result element(s).
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public static <T> T getMethodCallRecorder(Class<T> resultElementClass)
  {
    RecordingProxy<T> proxy = new RecordingProxy<T>(classLoader.get());
    methodCallRecorder.set(proxy.getInvocationRecorder());
    return proxy.getProxy(resultElementClass);
  }


  /**
   * Selects a certain set of objects in a given collection. The collection that
   * should be used must be specified in the returned {@link FromClause}. The
   * {@link FromClause} hereby returns a {@link WhereClause} with which an
   * arbitrary WHERE condition can be specified. This WHERE condition supports
   * AND, OR, the evaluation of user-defined {@link WhereCondition}s and
   * user-defined {@link ReflectiveWhereCondition}s.
   * 
   * @param <T> the collection element type.
   * @param resultElementClass the class of the result elements. This class is
   *          only necessary for type safety.
   * @return the FROM clause to specify the source collection for the query.
   */
  public static <T> FromClause<T, Iterable<T>> select(
      Class<T> resultElementClass)
  {
    QueryBuilder<T, Iterable<T>> queryBuilder = getQueryBuilder();
    Query<T, Iterable<T>> query = queryBuilder.createQuery();
    return query.createFromClause(resultElementClass);
  }


  /**
   * This method has basically the same functionality as {@link #select(Class)}.
   * But this method is not type safe regarding the returned result.
   * 
   * @param <T> the collection element type.
   * @return the FROM clause to specify the source collection of the query.
   */
  public static <T> FromClause<T, Iterable<T>> select()
  {
    QueryBuilder<T, Iterable<T>> queryBuilder = getQueryBuilder();
    Query<T, Iterable<T>> query = queryBuilder.createQuery();
    return query.createFromClause(new Class[0]);
  }


  /**
   * @param <T> the collection element type.
   * @return a new {@link QueryBuilder} instance.
   */
  private static <T> QueryBuilder<T, Iterable<T>> getQueryBuilder()
  {
    return new IterableQueryBuilder<T>(methodCallRecorder.get());
  }

}
