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

import org.jaqlib.core.*;
import org.jaqlib.iterable.FromClause;
import org.jaqlib.iterable.IterableQueryFactory;

import java.util.*;

/**
 * <h2>Overview</h2>
 * <p>
 * This class is the main entry point of Jaqlib for {@link Iterable} query
 * support. It provides following methods for building queries:
 * <ul>
 * <li>{@link #select()}</li>
 * <li>{@link #selectFrom(Iterable)}</li>
 * </ul>
 * </p>
 * 
 * 
 * <h2>Important issues</h2>
 * <ul>
 * <li>
 * The method call record mechanis uses <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/reflection/proxy.html">JDK
 * dynamic proxies</a> for proxying interfaces and <a
 * href="http://cglib.sourceforge.net/">CGLIB</a> for proxying classes. So if
 * you want to record method calls on classes you have to <a
 * href="https://sourceforge.net/project/showfiles.php?group_id=56933">
 * download</a> CGLIB and put it on the classpath of your application.</li>
 * <li>Various default values for querying {@link Iterable}s can be set
 * application-wide by using the <i>Jaqlib.List.DEFAULTS</i> object.</li>
 * <li>
 * This class is thread-safe.</li>
 * </ul>
 * 
 * 
 * <h2>Usage examples</h2>
 * <p>
 * The following examples use <i>Jaqlib.List</i> for accessing the
 * <i>IterableQueryBuilder</i>. Alternatively the class <i>IterableQB</i> can be
 * used, too.
 * </p>
 * 
 * 
 * <h3>Constraining the result</h3>
 * <p>
 * There are different ways how to constrain the returned query result. Jaqlib
 * provides an API for specifying WHERE clauses. You can use WHERE clauses in
 * three ways:
 * </p>
 * <ul>
 * <li>Method call recording mechanism</li>
 * <li>Custom where condition code</li>
 * <li>Simple comparison methods</li>
 * </ul>
 * 
 * <p>
 * <b>Method call recording mechanism</b><br>
 * By using the Method {@link #getRecorder(Class)} a recorder object is created
 * (= a JDK dynamic proxy). First the programmer must call the desired method on
 * the returned recorder object. This method call is recorded by JaQLib. When
 * JaqLib evaluates the WHERE condition this method call is replayed on every
 * selected element. The result of this method call is then evaluated against
 * the condition that is specified after the WHERE clause.
 * </p>
 * 
 * <pre>
 * // get accounts that should be queried
 * List&lt;Account&gt; accounts = getAccounts();
 * 
 * // get recorder object
 * Account account = Jaqlib.List.getRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 500.0
 * List&lt;Account&gt; result = Jaqlib.List.selectFrom(accounts)
 *     .whereCall(account.getBalance()).isGreaterThan(500.0).asList();
 * </pre>
 * 
 * <p>
 * In the example above the method call <tt>account.getBalance()</tt> is
 * recorded by Jaqlib. When Jaqlib executes the query this method is called on
 * every selected object. Each result of this method call is then evaluated
 * according to the given condition <tt>isGreaterThan(500.0)</tt>. Only the
 * selected elements that match this condition are returned.
 * </p>
 * 
 * <p>
 * <b>Custom where condition code</b><br>
 * By implementing the interface {@link WhereCondition} you can define your own
 * condition code. This alternative gives you most flexibility but is somewhat
 * cumbersome to implement (see example below).
 * </p>
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
 * // create condition for accounts with poor credit rating
 * WhereCondition&lt;Account&gt; ratingCondition = new WhereCondition&lt;Account&gt;()
 * {
 * 
 *   public boolean evaluate(Account account)
 *   {
 *     return (account.getCreditRating() == CreditRating.POOR);
 *   }
 * };
 * 
 * // execute query with these conditions
 * List&lt;Account&gt; highRiskAccounts = Jaqlib.List.selectFrom(accounts)
 *     .where(deptCondition).and(ratingCondition).asList();
 * </pre>
 * 
 * <p>
 * <b>Simple comparison methods</b><br>
 * There are some comparison methods that can be used for criteria matching (see
 * <a href=
 * "http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html"
 * >DataAccessObject<a>) or for matching primitive types.
 * </p>
 * 
 * You could use it for filtering {@link Comparable} elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used
 * // for comparing two accounts
 * AccountImpl criteria = new AccountImpl();
 * criteria.setBalance(5000.0);
 * 
 * List&lt;Account&gt; result = Jaqlib.List.selectFrom(accounts).where().element()
 *     .isSmallerThan(criteria).asList();
 * </pre>
 * 
 * Or you could use this mechanism to filter <tt>null</tt> elements:
 * 
 * <pre>
 * List&lt;Account&gt; notNullAccounts = Jaqlib.List.selectFrom(accounts).where()
 *     .element().isNotNull().asList();
 * </pre>
 * 
 * <h3>Using different result kinds</h3>
 * <p>
 * The result of a query can be returned in following ways:
 * <ul>
 * <li>as {@link List}
 * <li>as {@link Vector}
 * <li>as {@link Set}
 * <li>as {@link Map}
 * <li>as {@link Hashtable}
 * <li>as unique result
 * <li>as first occurrence
 * <li>as last occurrence
 * </ul>
 * </p>
 * <p>
 * <b>Return result as a Map or Hashtable:</b><br>
 * The key for the {@link Map} must be specified by using the method call
 * recording mechanism. Again a method call on a recorder object is recorded by
 * Jaqlib. When returning the query result Jaqlib executes this recorded method
 * on each selected element and uses the result as key of the map entry.
 * </p>
 * 
 * <pre>
 * // use the ID field of Account as key for the map
 * Account recorder = Jaqlib.List.getRecorder(Account.class);
 * Map&lt;Long, Account&gt; results = Jaqlib.List.selectFrom(accounts).asMap(
 *     recorder.getId());
 * </pre>
 * 
 * <p>
 * <b>Return result as Set:</b><br>
 * 
 * <pre>
 * Set&lt;Account&gt; notNullAccounts = Jaqlib.List.selectFrom(accounts).whereElement()
 *     .isNotNull().asSet();
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Return result as List or Vector:</b><br>
 * 
 * <pre>
 * List&lt;Account&gt; notNullAccounts = Jaqlib.List.selectFrom(accounts).whereElement()
 *     .isNotNull().asList();
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Return unique result:</b> <br>
 * Only one result is allowed to be selected by the query. If more than one
 * elements are selected then an {@link QueryResultException} is thrown. <br>
 * Note that you can use <tt>uniqueResult()</tt> or <tt>asUniqueResult()</tt> -
 * whatever you prefer better.
 * 
 * <pre>
 * Account recorder = Jaqlib.List.getRecorder(Account.class);
 * Account result = Jaqlib.List.selectFrom(accounts).whereCall(recorder.getId())
 *     .isEqual((long) 5).asUniqueResult();
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Return only the first result:</b> <br>
 * Only the element is returned that matches the given WHERE conditions first. <br>
 * Note that you can use <tt>firstResult()</tt> or <tt>asFirstResult()</tt> -
 * whatever you prefer better.
 * 
 * <pre>
 * Account recorder = Jaqlib.List.getRecorder(Account.class);
 * Account result = Jaqlib.List.selectFrom(accounts)
 *     .whereCall(recorder.getBalance()).isGreaterThan(500.0).asFirstResult();
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Return only the last result:</b> <br>
 * Only the element is returned that matches the given WHERE conditions last. <br>
 * Note that you can use <tt>lastResult()</tt> or <tt>asLastResult()</tt> -
 * whatever you prefer better.
 * 
 * <pre>
 * Account recorder = Jaqlib.List.getRecorder(Account.class);
 * Account result = Jaqlib.List.selectFrom(accounts)
 *     .whereCall(recorder.getBalance()).isGreaterThan(500.0).asLastResult();
 * </pre>
 * 
 * </p>
 * 
 * 
 * <h3>Executing a task on each element</h3>
 * <p>
 * You can also execute custom code (= {@link Task}) on each returned element.
 * </p>
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
 * <p>
 * You can also combine the task execution with all other previous examples. Two
 * examples are given below:
 * </p>
 * 
 * <pre>
 * // create condition for negative balances
 * WhereCondition&lt;Account&gt; deptCond = new WhereCondition&lt;Account&gt;()
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
 * @see IterableDefaults
 * @see IterableQB
 * @author Werner Fragner
 */
public class IterableQueryBuilder extends AbstractQueryBuilder
{

  /**
   * Contains the application wide default values for the Iterable query
   * builder.
   */
  public final IterableDefaults DEFAULTS = IterableDefaults.INSTANCE;


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
    IterableQueryFactory factory = new IterableQueryFactory(
        getMethodCallRecorder());
    return new FromClause(factory);
  }


  /**
   * Shortcut method for <tt>select().from()</tt>.
   * 
   * @param iterable
   *          a not null Iterable for the query.
   * @return a where clause for defining the query conditions.
   */
  public <T> WhereClause<T, Iterable<T>> selectFrom(Iterable<T> iterable)
  {
    return select().from(iterable);
  }

}
