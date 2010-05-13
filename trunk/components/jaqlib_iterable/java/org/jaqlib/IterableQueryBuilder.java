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

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.WhereClause;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.iterable.FromClause;
import org.jaqlib.iterable.IterableQuery;

/**
 * <p>
 * The main entry point of JaQLib for {@link Iterable} support. It provides
 * {@link #select()} and {@link #selectFrom(Iterable)} for building queries.
 * </p>
 * <p>
 * The Method {@link #getRecorder(Class)} can be used to define a WHERE
 * condition using a method call recording mechanism (see also the first example
 * below). First the programmer must call the desired method on the returned
 * proxy object. This method call is recorded by JaQLib. When JaqLib evaluates
 * the WHERE condition this method call is replayed on every selected element.
 * The result of this method call is then evaluated against the specified
 * condition.
 * </p>
 * This class is thread-safe.
 * <p>
 * <b>Usage examples:</b><br>
 * <p>
 * The following examples use Jaqlib.List for accessing the
 * IterableQueryBuilder. Alternatively the class IterableQB can be used, too.
 * </p>
 * <i>Method call recording mechanism:</i>
 * 
 * <pre>
 * // get accounts that should be queried
 * List&lt;Account&gt; accounts = getAccounts();
 * 
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = Jaqlib.List.getRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 5000
 * List&lt;Account&gt; result = Jaqlib.List.selectFrom(accounts).whereCall(
 *     account.getBalance()).isGreaterThan(5000).asList();
 * </pre>
 * 
 * <i>Custom WHERE conditions:</i>
 * 
 * <pre>
 * // create condition for negative balances
 * WhereCondition&lt;Account&gt; deptCondition = new WhereCondition&lt;Account&gt;() {
 * 
 *   public boolean evaluate(Account account) {
 *     return (account.getBalance() &lt; 0);
 *   }
 * 
 * };
 * 
 * // create condition for accounts with poor credit rating
 * WhereCondition&lt;Account&gt; ratingCondition = new WhereCondition&lt;Account&gt;() {
 * 
 *   public boolean evaluate(Account account) {
 *     return (account.getCreditRating() == CreditRating.POOR);
 *   }
 * }
 * 
 * // execute query with these conditions 
 * List&lt;Account&gt; highRiskAccounts = Jaqlib.List.selectFrom(accounts)
 *     .where(deptCondition).and(ratingCondition).asList();
 * </pre>
 * 
 * <i>Filtering null elements:</i>
 * 
 * <pre>
 * List&lt;Account&gt; notNullAccounts = Jaqlib.List.selectFrom(accounts).where()
 *     .element().isNotNull().asList();
 * </pre>
 * 
 * <i>Filtering {@link Comparable} elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used for comparing two accounts
 * AccountImpl spec = new AccountImpl();
 * account.setBalance(5000);
 * 
 * List&lt;Account&gt; result = Jaqlib.List.selectFrom(accounts).where().element()
 *     .isSmallerThan(spec).asList();
 * </pre>
 * 
 * <i>Map as result:</i>
 * 
 * <pre>
 * Account account = Jaqlib.List.getRecorder(Account.class);
 * Map&lt;Long, Account&gt; results = Jaqlib.List.selectFrom(accounts).asMap(
 *     account.getId());
 * </pre>
 * 
 * <i>Executing a task on each element:</i>
 * 
 * <pre>
 * // create task that should be executed for each element
 * Task&lt;Account&gt; task = new Task&lt;Account&gt;()
 * {
 * 
 *   public void execute(Account account)
 *   {
 *     account.sendInfoEmail();
 *   }
 * 
 * };
 * Jaqlib.List.selectFrom(accounts).execute(task);
 * </pre>
 * 
 * <pre>
 * // create condition for negative balances
 * WhereCondition&lt;Account&gt; deptCondition = new WhereCondition&lt;Account&gt;()
 * {
 * 
 *   public boolean evaluate(Account account)
 *   {
 *     return (account.getBalance() &lt; 0);
 *   }
 * 
 * };
 * 
 * // execute task only on elements that match the given condition 
 * Jaqlib.List.selectFrom(accounts).where(deptCond).execute(task);
 * 
 * // or ...
 * List&lt;Account&gt; result = Jaqlib.List.selectFrom(accounts).where(deptCond)
 *     .executeWithResult(task).asList();
 * </pre>
 * 
 * </p>
 * 
 * @author Werner Fragner
 */
public class IterableQueryBuilder extends AbstractQueryBuilder
{

  /**
   * Contains the application wide default values for the Iterable query
   * builder.
   */
  public static final IterableDefaults DEFAULTS = IterableDefaults.INSTANCE;


  /**
   * Selects a certain set of objects in a given collection. The collection that
   * should be used must be specified in the returned {@link FromClause}. The
   * {@link FromClause} hereby returns a {@link WhereClause} that can be used to
   * specify an arbitrary WHERE condition. This WHERE condition supports AND and
   * OR connectors, the evaluation of custom {@link WhereCondition}s and custom
   * conditions using a method call recording mechanism (see examples and
   * {@link #getRecorder(Class)} for further details).
   * 
   * @return the FROM clause to specify the source collection for the query.
   */
  public FromClause select()
  {
    return new FromClause(this);
  }


  /**
   * Shortcut method for <tt>select().from()</tt>.
   * 
   * @param iterable a not null Iterable for the query.
   * @return a where clause for defining the query conditions.
   */
  public <T> WhereClause<T, Iterable<T>> selectFrom(Iterable<T> iterable)
  {
    return select().from(iterable);
  }


  /**
   * @param <T> the element type of the {@link Iterable}.
   * @return a query for using the functionality of JaQLib without the fluent
   *         API.
   */
  public <T> IterableQuery<T> createQuery()
  {
    return new IterableQuery<T>(getMethodCallRecorder());
  }

}
