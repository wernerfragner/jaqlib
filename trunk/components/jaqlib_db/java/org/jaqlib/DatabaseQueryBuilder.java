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

import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.WhereClause;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.AbstractMapping;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.db.ColumnMapping;
import org.jaqlib.db.DbDefaults;
import org.jaqlib.db.DbFromClause;
import org.jaqlib.db.DbQuery;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.DeleteFromClause;
import org.jaqlib.db.InClause;
import org.jaqlib.db.IntoClause;
import org.jaqlib.db.Using;

/**
 * <p>
 * The main entry point of JaQLib for database query support. It provides
 * following methods for building queries:
 * <ul>
 * <li>{@link #select(ColumnMapping)}</li>
 * <li>{@link #select(Class)}</li>
 * <li>{@link #select(BeanMapping)}</li>
 * <li>{@link #insert(Object)}</li>
 * <li>{@link #update(Object)}</li>
 * <li>{@link #delete()}</li>
 * </ul>
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
 * All examples use following statements to define the database connection and
 * the SQL statement that should act as data source for some bank accounts.<br>
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * DbSelectDataSource accounts = Jaqlib.DB.getSelectDataSource(
 *     getJdbcDataSource(), sql);
 * </pre>
 * 
 * or if multiple SQL SELECT statements should be executed against the same JDBC
 * {@link DataSource}:
 * 
 * <pre>
 * DataSource ds = getJdbcDataSource();
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * DbSelectDataSource accounts = Jaqlib.DB.getSelectDataSource(ds, sql);
 * </pre>
 * 
 * or if the simplified API should be used:
 * 
 * <pre>
 * DataSource ds = getJdbcDataSource();
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * Jaqlib.DB.select(AccountImpl.class).from(ds, sql).where(...)
 * </pre>
 * 
 * <i>Method call recording mechanism:</i>
 * 
 * <pre>
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = Jaqlib.DB.getRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 5000
 * List&lt;AccountImpl&gt; results = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .whereCall(account.getBalance()).isGreaterThan(5000).asList();
 * </pre>
 * 
 * <i>Custom WHERE conditions:</i>
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
 * List&lt;AccountImpl&gt; highRiskAccounts = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .where(deptCondition).and(ratingCondition).asList();
 * </pre>
 * 
 * <i>Filtering null elements:</i>
 * 
 * <pre>
 * List&lt;AccountImpl&gt; notNullAccounts = Jaqlib.DB.select(AccountImpl.class).from(
 *     accounts).where().element().isNotNull().asList();
 * </pre>
 * 
 * <i>Filtering {@link Comparable} elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used for comparing two accounts
 * AccountImpl spec = new AccountImpl();
 * account.setBalance(5000);
 * 
 * List&lt;AccountImpl&gt; result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .where().element().isSmallerThan(spec).asList();
 * </pre>
 * 
 * <i>Map as result:</i>
 * 
 * <pre>
 * Account account = Jaqlib.DB.getRecorder(Account.class);
 * Map&lt;Long, AccountImpl&gt; results = Jaqlib.DB.select(AccountImpl.class).from(
 *     accounts).asMap(account.getId());
 * </pre>
 * 
 * <i>Select all account with some constraints using a {@link PreparedStatement}
 * :</i>
 * <p>
 * If the same {@link PreparedStatement} should be used for multiple queries
 * then a PreparedStatement pool (like http://commons.apache.org/dbcp/) must be
 * used. Alternatively following code example can be used. It's important to set
 * the property <tt>AutoClosePreparedStatement</tt> to false and to close the
 * {@link DbSelectDataSource} after having issued the queries. By default the
 * <tt>AutoClosePreparedStatement</tt> is set to true.
 * </p>
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT WHERE fname = ? AND balance &gt; ?&quot;;
 * DbSelectDataSource accounts = Jaqlib.DB.getSelectDataSource(getJdbcDataSource(),
 *     sql); 
 * accounts.setAutoClosePreparedStatement(false);
 * 
 * try 
 * {
 *   List&lt;AccountImpl&gt; results1000 = Jaqlib.DB.select(AccountImpl.class).from(
 *     accounts).using(&quot;werner&quot;, 1000).asList();
 *     
 *   List&lt;AccountImpl&gt; results2000 = Jaqlib.DB.select(AccountImpl.class).from(
 *     accounts).using(&quot;werner&quot;, 2000).asList();
 *     
 *   List&lt;AccountImpl&gt; results3000 = Jaqlib.DB.select(AccountImpl.class).from(
 *     accounts).using(&quot;werner&quot;, 3000).asList());
 * } 
 * finally 
 * {    
 *   // close all used statements (in that case it's only one statement)
 *   accounts.close();
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * <i>Custom Java type handler:</i>
 * <p>
 * Jaqlib.DB column data types can be converted to custom Java types with
 * so-called {@link JavaTypeHandler}s. These handlers can be registered using
 * {@link BeanMapping#registerJavaTypeHandler(JavaTypeHandler)} .
 * </p>
 * <p>
 * The <tt>AccountImpl</tt> class has a <tt>creditRating</tt> field with the
 * custom enumeration type <tt>CreditRating</tt>. At Jaqlib.DB this field is
 * stored as an Integer value. By using a {@link JavaTypeHandler} this Integer
 * value is converted into the according <tt>CreditRating</tt> enumeration
 * value.
 * </p>
 * 
 * <pre>
 * // get DbSelectDataSource and BeanMapping
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * DataSource ds = getJdbcDataSource();
 * DbSelectDataSource dataSource = Jaqlib.DB.getSelectDataSource(ds, sql);
 * BeanMapping&lt;AccountImpl&gt; mapping = db.getBeanMapping(AccountImpl.class);
 * 
 * // register custom type handler for CreditRating bean fields
 * mapping.registerJavaTypeHandler(new CreditRatingTypeHandler());
 * 
 * // perform query 
 * Jaqlib.DB.select(mapping).from(dataSource) ...
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
 * Jaqlib.DB.select(Account.class).from(accounts).execute(task);
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
 * Jaqlib.DB.select(Account.class).from(accounts).where(deptCond).execute(task);
 * 
 * // or ...
 * List&lt;Account&gt; result = Jaqlib.DB.select(Account.class).from(accounts).where(
 *     deptCond).executeWithResult(task).asList();
 * </pre>
 * 
 * <i>Inserting a Java bean into a Jaqlib.DB table:</i>
 * 
 * <pre>
 * AccountImpl account = new AccountImpl();
 * // fill account with values ...
 * 
 * String tableName = &quot;ACCOUNT&quot;;
 * DbInsertDataSource dataSource = Jaqlib.DB.getInsertDataSource(
 *     getJdbcDataSource(), tableName);
 * int updateCount = Jaqlib.DB.insert(account).into(dataSource)
 *     .usingDefaultMapping();
 * </pre>
 * 
 * <i>Inserting a Java bean into a Jaqlib.DB table using a custom bean
 * mapping:</i>
 * 
 * <pre>
 * AccountImpl account = new AccountImpl();
 * // fill account with values ...
 * 
 * // create custom bean mapping
 * BeanMapping&lt;AccountImpl&gt; beanMapping = Jaqlib.DB
 *     .getDefaultBeanMapping(AccountImpl.class);
 * beanMapping.removeChildColumn(&quot;id&quot;);
 * beanMapping.getChildColumn(&quot;lastName&quot;).setColumnName(&quot;lName&quot;);
 * 
 * // insert account using the custom bean mapping and the simplified API
 * String tableName = &quot;ACCOUNT&quot;;
 * int updateCount = Jaqlib.DB.insert(account)
 *     .into(getJdbcDataSource(), tableName).using(beanMapping);
 * </pre>
 * 
 * <i>Updating a Java bean in a Jaqlib.DB table:</i>
 * 
 * <pre>
 * // get account that already exists at Jaqlib.DB 
 * AccountImpl account = getAccount();
 * 
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * DataSource ds = getJdbcDataSource();
 * DbUpdateDataSource dataSource = Jaqlib.DB.getUpdateDataSource(ds, tableName);
 * int updateCount = Jaqlib.DB.update(account).in(dataSource).where(whereClause)
 *     .usingDefaultMapping();
 * </pre>
 * 
 * <i>Updating a Java bean in a Jaqlib.DB table using a custom bean mapping:</i>
 * 
 * <pre>
 * // get account that already exists at Jaqlib.DB 
 * AccountImpl account = getAccount();
 * 
 * // create custom bean mapping
 * BeanMapping&lt;AccountImpl&gt; beanMapping = Jaqlib.DB
 *     .getDefaultBeanMapping(AccountImpl.class);
 * beanMapping.removeChildColumn(&quot;id&quot;);
 * beanMapping.getChildColumn(&quot;lastName&quot;).setColumnName(&quot;lName&quot;);
 * 
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * int updateCount = Jaqlib.DB.update(account).in(getJdbcDataSource(), tableName,
 *     ).where(whereClause).using(beanMapping);
 * </pre>
 * 
 * <i>Delete specific records from a Jaqlib.DB table:</i>
 * 
 * <pre>
 * // get account that already exists at Jaqlib.DB 
 * AccountImpl account = getAccount();
 * 
 * // delete account at Jaqlib.DB
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * DataSource ds = getJdbcDataSource();
 * DbDeleteDataSource dataSource = Jaqlib.DB.getDeleteDataSource(ds, tableName,
 *     whereClause);
 * int updateCount = Jaqlib.DB.delete().from(dataSource);
 * </pre>
 * 
 * <i>Delete all records from a Jaqlib.DB table:</i>
 * 
 * <pre>
 * // delete all records from the ACCOUNT table 
 * String tableName = &quot;ACCOUNT&quot;;
 * int updateCount = Jaqlib.DB.delete().from(getJdbcDataSource(), tableName);
 * </pre>
 * 
 * </p>
 * 
 * @see org.jaqlib.Database
 * @see org.jaqlib.DatabaseQB
 * @see org.jaqlib.Jaqlib.DB
 * @author Werner Fragner
 */
public class DatabaseQueryBuilder extends AbstractQueryBuilder
{

  /**
   * Contains the application wide default values for the database query
   * builder.
   */
  public static final DbDefaults DEFAULTS = DbDefaults.INSTANCE;


  /**
   * <p>
   * Selects one column of a given database SELECT statement. The SELECT
   * statement that should be used must be specified in the returned
   * {@link DbFromClause}. The {@link DbFromClause} hereby returns a
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
   * @param columnMapping an object defining the desired column.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> DbFromClause<T> select(ColumnMapping<T> columnMapping)
  {
    return new DbFromClause<T>(this, columnMapping);
  }


  /**
   * <p>
   * Uses a given database SELECT statement to fill a user-defined Java bean.
   * The SELECT statement that should be used must be specified in the returned
   * {@link DbFromClause}. The {@link DbFromClause} hereby returns a
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
   *          constructor. If the bean does not provide one a custom
   *          {@link BeanFactory} must be registered at the {@link BeanMapping}.
   *          This {@link BeanMapping} can be obtained from {@link DatabaseQB}
   *          and can be used with {@link #select(BeanMapping)}.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> DbFromClause<T> select(Class<? extends T> beanClass)
  {
    BeanMapping<T> beanMapping = Database.getDefaultBeanMapping(beanClass);
    return select(beanMapping);
  }


  /**
   * This method basically provides the same functionality as
   * {@link #select(Class)}. But it gives more flexibility in defining the
   * mapping between SELECT statement results to Java bean instance fields. This
   * mapping can defined with a {@link BeanMapping} instance. For building these
   * instances see {@link DatabaseQB}.
   * 
   * @param <T> the result bean type.
   * @param beanMapping a bean definition that holds information how to map the
   *          result of the SELECT statement to a Java bean.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> DbFromClause<T> select(BeanMapping<T> beanMapping)
  {
    return new DbFromClause<T>(this, beanMapping);
  }


  /**
   * @param <T> the element type of the data source.
   * @return a query for using the functionality of JaQLib without the fluent
   *         API.
   */
  public <T> DbQuery<T> createQuery(AbstractMapping<T> mapping)
  {
    return new DbQuery<T>(getMethodCallRecorder(), mapping);
  }


  /**
   * Inserts the given bean into the table that must be specified in the
   * returned {@link IntoClause}. The mapping between the given bean field
   * values to the according table columns must be specified in the
   * {@link Using} object that is returned by
   * {@link IntoClause#into(org.jaqlib.db.DbInsertDataSource)}.
   * 
   * @param <T> the type of the bean.
   * @param bean a not null bean to insert.
   * @return the INTO clause to specify the table where to insert the bean.
   */
  public <T> IntoClause<T> insert(T bean)
  {
    return new IntoClause<T>(bean);
  }


  /**
   * Updates the given bean in the table that must be specified in the returned
   * {@link InClause}. The mapping between the given bean field values to the
   * according table columns must be specified in the {@link Using} object that
   * is returned by {@link InClause#in(org.jaqlib.db.DbUpdateDataSource)}.
   * 
   * @param <T> the type of the bean.
   * @param bean a not null bean to update.
   * @return the IN clause to specify the table where to update the bean.
   */
  public <T> InClause<T> update(T bean)
  {
    return new InClause<T>(bean);
  }


  /**
   * Deletes a table that is specified in the returned {@link DeleteFromClause}.
   * 
   * @return the FROM clause to specify the table where to delete records.
   */
  public DeleteFromClause delete()
  {
    return new DeleteFromClause();
  }


}
