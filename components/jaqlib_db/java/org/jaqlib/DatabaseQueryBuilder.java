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

import javax.sql.DataSource;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.FromClause;
import org.jaqlib.core.WhereClause;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.db.BeanMapping;
import org.jaqlib.db.ColumnMapping;
import org.jaqlib.db.DatabaseQuery;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.java.typehandler.JavaTypeHandler;

/**
 * <p>
 * The main entry point of JaQLib for database query support. It provides
 * following methods for building queries:
 * <ul>
 * <li>{@link #select(ColumnMapping)}</li>
 * <li>{@link #select(Class)}</li>
 * <li>{@link #select(BeanMapping)}</li>
 * </ul>
 * </p>
 * <p>
 * The Method {@link #getRecorder(Class)} can be used to define a WHERE
 * condition using a method call recording mechanism (see also the first example
 * below). First the programmer must call the desired method on the returned
 * proxy object. This method call is recorded by JaQLib. When evaluating the
 * WHERE condition this method call is replayed on every selected element. The
 * result of this method call is then evaluated against the specified condition.
 * </p>
 * This class is thread-safe.
 * <p>
 * <b>Usage examples:</b><br>
 * All examples use following statements to define the database connection and
 * the SQL SELECT statement that should act as data source for some bank
 * accounts.<br>
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * DbSelectDataSource accounts = Database.getSelectDataSource(getJdbcDataSource(),
 *     sql);
 * </pre>
 * 
 * or (if multiple SQL SELECT statements should be executed against the same
 * JDBC {@link DataSource}).
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * Database db = new Database(getJdbcDataSource());
 * DbSelectDataSource accounts = db.getSelectDataSource(sql);
 * </pre>
 * 
 * <i>Example using the method call recording mechanism:</i>
 * 
 * <pre>
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = DatabaseQB.getRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 5000
 * List&lt;AccountImpl&gt; results = DatabaseQB.select(AccountImpl.class).from(accounts)
 *     .where(account.getBalance()).isGreaterThan(5000).asList();
 * </pre>
 * 
 * <i>Example with custom WHERE conditions:</i>
 * 
 * <pre>
 * // create condition for negative balances
 * WhereCondition&lt;AccountImpl&gt; deptCondition = new WhereCondition&lt;AccountImpl&gt;() {
 * 
 *   public boolean evaluate(AccountImpl account) {
 *     return (account.getBalance() &lt; 0);
 *   }
 * 
 * };
 * 
 * // create condition for accounts with poor credit rating
 * WhereCondition&lt;AccountImpl&gt; ratingCondition = new WhereCondition&lt;AccountImpl&gt;() {
 * 
 *   public boolean evaluate(AccountImpl account) {
 *     return (account.getCreditRating() == CreditRating.POOR);
 *   }
 * }
 * 
 * // execute query with these conditions 
 * List&lt;AccountImpl&gt; highRiskAccounts = DatabaseQB.select(AccountImpl.class).from(accounts)
 *     .where(deptCondition).and(ratingCondition).asList();
 * </pre>
 * 
 * <i>Example for filtering out null elements:</i>
 * 
 * <pre>
 * List&lt;AccountImpl&gt; notNullAccounts = DatabaseQB.select(AccountImpl.class).from(
 *     accounts).where().element().isNotNull().asList();
 * </pre>
 * 
 * <i>Example for using {@link Comparable} elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used for comparing two accounts
 * AccountImpl spec = new AccountImpl();
 * account.setBalance(5000);
 * 
 * List&lt;AccountImpl&gt; result = DatabaseQB.select(AccountImpl.class).from(accounts)
 *     .where().element().isSmallerThan(spec).asList();
 * </pre>
 * 
 * <i>Example using a Map as result:</i>
 * 
 * <pre>
 * Account account = DatabaseQB.getRecorder(Account.class);
 * Map&lt;Long, AccountImpl&gt; results = DatabaseQB.select(AccountImpl.class).from(
 *     accounts).asMap(account.getId());
 * </pre>
 * 
 * </p>
 * <p>
 * 
 * Database column data types can be converted to custom Java types with
 * so-called {@link JavaTypeHandler}s. These handlers can be registered with
 * {@link BeanMapping#registerJavaTypeHandler(Class, JavaTypeHandler)} .
 * </p>
 * <p>
 * <i>Example for custom java type handler:</i>
 * 
 * <pre>
 * // get DbSelectDataSource and BeanMapping
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * Database db = new Database(getDataSource());
 * DbSelectDataSource dataSource = db.getSelectDataSource(sql);
 * BeanMapping&lt;AccountImpl&gt; mapping = db.getBeanMapping(AccountImpl.class);
 * 
 * // register custom type handler for CreditRating bean fields
 * mapping.registerJavaTypeHandler(CreditRating.class,
 *     new CreditRatingTypeHandler());
 * 
 * // perform query 
 * DatabaseQB.select(mapping).from(dataSource) ...
 * 
 * // custom java type handler that converts Integer values from DB into CreditRating enumerations  
 * public class CreditRatingTypeHandler extends AbstractJavaTypeHandler
 * {
 *   public Object getObject(Object value)
 *   {
 *     if (value instanceof Integer)
 *       return CreditRating.rating((Integer) value);
 *     else
 *       throw handleIllegalInputValue(value, CreditRating.class);
 *   }
 * }
 * 
 * </pre>
 * 
 * <i>Examples with executing a task on each element:</i>
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
 * DatabaseQB.select(Account.class).from(accounts).execute(task);
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
 * DatabaseQB.select(Account.class).from(accounts).where(deptCond).execute(task);
 * 
 * // or ...
 * List&lt;Account&gt; result = DatabaseQB.select(Account.class).from(accounts).where(
 *     deptCond).executeWithResult(task).asList();
 * </pre>
 * 
 * </p>
 * 
 * @see DatabaseQB
 * @see Database
 * @author Werner Fragner
 */
public class DatabaseQueryBuilder extends AbstractQueryBuilder
{

  /**
   * <p>
   * Selects one column of a given database SELECT statement. The SELECT
   * statement that should be used must be specified in the returned
   * {@link FromClause}. The {@link FromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of custom {@link WhereCondition}s and custom conditions using a
   * method call recording mechanism (see examples and
   * {@link #getRecorder(Class)} for further details).
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed at database-side but at Java
   * side. Avoid executing SELECT statements with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result column type.
   * @param column an object defining the desired column.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> FromClause<T, DbSelectDataSource> select(ColumnMapping<T> column)
  {
    return this.<T> createQuery().createFromClause(column);
  }


  /**
   * <p>
   * Uses a given database SELECT statement to fill a user-defined Java bean.
   * The SELECT statement that should be used must be specified in the returned
   * {@link FromClause}. The {@link FromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of custom {@link WhereCondition}s and custom conditions using a
   * method call recording mechanism (see examples and
   * {@link #getRecorder(Class)} for further details).
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed at database-side but at Java
   * side. Avoid executing SELECT statements with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result bean type.
   * @param beanClass the desired result bean. This bean must provide a default
   *          constructor (otherwise a {@link RuntimeException} is thrown).
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> FromClause<T, DbSelectDataSource> select(
      Class<? extends T> beanClass)
  {
    BeanMapping<T> beanResult = Database.getDefaultBeanMapping(beanClass);
    return select(beanResult);
  }


  /**
   * This method basically provides the same functionality as
   * {@link #select(Class)}. But it gives more flexibility in defining the
   * mapping between SELECT statement results to Java bean instance fields. This
   * mapping can defined with a {@link BeanMapping} instance. For build these
   * instances see {@link Database}.
   * 
   * @param <T> the result bean type.
   * @param bean a bean definition that holds information how to map the result
   *          of the SELECT statement to a Java bean.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> FromClause<T, DbSelectDataSource> select(BeanMapping<T> bean)
  {
    return this.<T> createQuery().createFromClause(bean);
  }


  private <T> DatabaseQuery<T> createQuery()
  {
    return new DatabaseQuery<T>(getMethodCallRecorder());
  }


}
