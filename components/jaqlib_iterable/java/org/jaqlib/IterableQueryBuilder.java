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
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.query.iterable.IterableQuery;
import org.jaqlib.reflect.MethodCallRecorder;
import org.jaqlib.reflect.RecordingProxy;
import org.jaqlib.util.Assert;

/**
 * <p>
 * The main entry point of JaQLib for {@link Iterable} support. It provides
 * methods for building queries ( {@link #select(Class)}, {@link #select()}) and
 * adapting the query building process ( {@link #setClassLoader(ClassLoader)}
 * ).</br> The Method {@link #getMethodCallRecorder(Class)} can be used to
 * define a WHERE condition where a return value of method call is compared to
 * an other value (see also the first example below).
 * </p>
 * <p>
 * <b>Usage examples:</b><br>
 * <i>Example with method call recording:</i>
 * 
 * <pre>
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = QB.getMethodCallRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 5000
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
 * <i>Example using a Map as result:</i>
 * 
 *<pre>
 * Account account = QB.getMethodCallRecorder(Account.class);
 * Map&lt;String, Account&gt; results = QB.select(Account.class).from(accounts).toMap(
 *     account.getId());
 * </pre>
 * 
 * </p>
 * 
 * @author Werner Fragner
 */
public class IterableQueryBuilder
{

  private final ThreadLocal<MethodCallRecorder> methodCallRecorder = new ThreadLocal<MethodCallRecorder>();
  private final ThreadLocal<ClassLoader> classLoader = new ThreadLocal<ClassLoader>();


  public IterableQueryBuilder(MethodCallRecorder methodCallRecorder)
  {
    this();
    Assert.notNull(methodCallRecorder);
    this.methodCallRecorder.set(methodCallRecorder);
  }


  /**
   * Initializes this class with the a default class loader.
   */
  public IterableQueryBuilder()
  {
    classLoader.set(getClass().getClassLoader());
  }


  /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getMethodCallRecorder(Class)} method.
   * 
   * @param classLoader a not null class loader.
   */
  public void setClassLoader(ClassLoader classLoader)
  {
    Assert.notNull(classLoader);
    this.classLoader.set(classLoader);
  }


  /**
   * Package private because client code should not be able to access this
   * method. This method is only meant for internal usage and for usage by the
   * static helper class {@link QB}.
   * 
   * @param <T> the type of the result element(s).
   * @return a query object.
   */
  <T> IterableQuery<T> createQuery()
  {
    return new IterableQuery<T>(methodCallRecorder.get());
  }


  /**
   * @param <T> the type of the result element(s).
   * @param resultElementClass a not null class of the result element(s).
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public <T> T getMethodCallRecorder(Class<T> resultElementClass)
  {
    RecordingProxy<T> proxy = new RecordingProxy<T>(classLoader.get());
    methodCallRecorder.set(proxy.getInvocationRecorder());
    return proxy.getProxy(resultElementClass);
  }


  /**
   * Selects a certain set of objects in a given collection. The collection that
   * should be used must be specified in the returned {@link FromClause}. The
   * {@link FromClause} hereby returns a {@link WhereClause} that can be used to
   * specify an arbitrary WHERE condition. This WHERE condition supports AND and
   * OR connectors, the evaluation of user-defined {@link WhereCondition}s and
   * user-defined {@link ReflectiveWhereCondition}s.
   * 
   * @param <T> the collection element type.
   * @param resultElementClass the class of the result elements. This class is
   *          only necessary for type safety.
   * @return the FROM clause to specify the source collection for the query.
   */
  public <T> FromClause<T, Iterable<T>> select(Class<T> resultElementClass)
  {
    return this.<T> createQuery().createFromClause(resultElementClass);
  }


  /**
   * This method has basically the same functionality as {@link #select(Class)}.
   * But this method is not type safe regarding the returned result.
   * 
   * @return the FROM clause to specify the source collection of the query.
   */
  public FromClause<Object, Iterable<Object>> select()
  {
    return createQuery().createFromClause(Object.class);
  }

}
