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
import org.jaqlib.core.FromClause;
import org.jaqlib.core.ReflectiveWhereCondition;
import org.jaqlib.core.WhereClause;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.iterable.IterableQuery;

/**
 * <p>
 * The main entry point of JaQLib for {@link Iterable} support. It provides
 * following methods for building queries:
 * <ul>
 * <li>{@link #select(Class)}</li>
 * <li>{@link #select()}</li>
 * </ul>
 * </p>
 * <p>
 * The Method {@link #getMethodCallRecorder(Class)} can be used to define a
 * WHERE condition using a method call recording mechanism (see also the first
 * example below). First the programmer must call the desired method on the
 * returned proxy object. This method call is recorded by JaQLib. When
 * evaluating the WHERE condition this method call is replayed on every element.
 * The result of this method call is then evaluated against the specified
 * condition.
 * </p>
 * This class is thread-safe.
 * <p>
 * <b>Usage examples:</b><br>
 * <i>Example using the method call recording mechanism:</i>
 * 
 * <pre>
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = IterableQB.getMethodCallRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 5000
 * List&lt;Account&gt; result = IterableQB.select(Account.class).from(accounts).where(
 *     account.getBalance()).isGreaterThan(5000).asList();
 * </pre>
 * 
 * <i>Example with custom WHERE conditions:</i>
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
 * List&lt;Account&gt; highRiskAccounts = IterableQB.select(Account.class).from(accounts)
 *     .where(deptCondition).and(ratingCondition).asList();
 * </pre>
 * 
 * <i>Example for filtering out null elements:</i>
 * 
 * <pre>
 * List&lt;Account&gt; notNullAccounts = IterableQB.select(Account.class).from(accounts)
 *     .where().element().isNotNull().asList();
 * </pre>
 * 
 * <i>Example for using {@link Comparable} elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used for comparing two accounts
 * AccountImpl spec = new AccountImpl();
 * account.setBalance(5000);
 * 
 * List&lt;Account&gt; result = IterableQB.select(Account.class).from(accounts).where()
 *     .element().isSmallerThan(spec).asList();
 * </pre>
 * 
 * <i>Example using a Map as result:</i>
 * 
 * <pre>
 * Account account = IterableQB.getMethodCallRecorder(Account.class);
 * Map&lt;Long, Account&gt; results = IterableQB.select(Account.class).from(accounts)
 *     .asMap(account.getId());
 * </pre>
 * 
 * </p>
 * 
 * @author Werner Fragner
 */
public class IterableQueryBuilder extends AbstractQueryBuilder
{

  /**
   * Selects a certain set of objects in a given collection. The collection that
   * should be used must be specified in the returned {@link FromClause}. The
   * {@link FromClause} hereby returns a {@link WhereClause} that can be used to
   * specify an arbitrary WHERE condition. This WHERE condition supports AND and
   * OR connectors, the evaluation of custom {@link WhereCondition}s and custom
   * {@link ReflectiveWhereCondition}s.
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


  private <T> IterableQuery<T> createQuery()
  {
    return new IterableQuery<T>(getMethodCallRecorder());
  }

}
