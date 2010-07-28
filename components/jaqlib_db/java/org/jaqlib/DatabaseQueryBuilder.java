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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.sql.DataSource;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.QueryResultException;
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
 * <h2>Overview</h2>
 * <p>
 * This class is the main entry point of Jaqlib for database query support. It
 * provides following methods for building queries:
 * <ul>
 * <li>{@link #select(Class)}</li>
 * <li>{@link #select(ColumnMapping)}</li>
 * <li>{@link #select(BeanMapping)}</li>
 * <li>{@link #insert(Object)}</li>
 * <li>{@link #update(Object)}</li>
 * <li>{@link #delete()}</li>
 * </ul>
 * </p>
 * 
 * <h2>Important issues</h2>
 * <ul>
 * <li>
 * The Jaqlib WHERE condition is not executed at database but at Java side.
 * Avoid executing SELECT statements that return lots of data and then
 * constraining the result with the WHERE functionality of JaqLib!</li>
 * <li>
 * The mapping between database columns to Java bean fields can be adapted by
 * using a {@link BeanMapping} object. See example 'Define a custom bean
 * mapping' for further details.</li>
 * <li>Jaqlib does <b>NOT</b> close database connection automatically. It just
 * obtains the {@link Connection} object from the {@link DataSource} and uses it
 * without closing it. If Jaqlib should close the {@link Connection} after
 * performing the query then {@link DbDefaults#setAutoCloseConnection(boolean)}
 * must be called. Alternatively the auto close functionality can be activated
 * by calling {@link DbSelectDataSource#setAutoCloseConnection(boolean)}.</li>
 * <li>Jaqlib does close {@link PreparedStatement} automatically after executing
 * the query. If Jaqlib should not close the {@link PreparedStatement} after
 * performing the query then
 * {@link DbDefaults#setAutoClosePreparedStatement(boolean)} must be called.
 * Alternatively the auto close functionality can be deactivated by calling
 * {@link DbSelectDataSource#setAutoClosePreparedStatement(boolean)}.</li>
 * <li>
 * Various default values for accessing a database can be set application-wide
 * by using the {@link #DEFAULTS} object.</li>
 * <li>This class is thread-safe.</li>
 * </ul>
 * 
 * 
 * <h2>Usage examples</h2> All examples use following statements to define the
 * database connection and the SQL statement that should act as data source for
 * some bank accounts.<br>
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * DbSelectDataSource accounts = new DbSelectDataSource(getJdbcDataSource(), sql);
 * </pre>
 * 
 * alternatively following API can be used, as well:
 * 
 * <pre>
 * DataSource ds = getJdbcDataSource();
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * 
 * Jaqlib.DB.select(AccountImpl.class).from(ds, sql).where(...)
 * </pre>
 * 
 * 
 * <h3>Selecting primitive values</h3> If you just want to select one or a list
 * of primitive values you can use following code:
 * 
 * <pre>
 * String sql = &quot;select count(*) from accounts&quot;;
 * DbSelectDataSource dataSource = new DbSelectDataSource(getDataSource(), sql);
 * 
 * int nrRecords = Jaqlib.DB.select(Integer.class).from(dataSource).uniqueResult();
 * </pre>
 * 
 * <h3>Selecting Java objects</h3>
 * <p>
 * Jaqlib supports mapping database columns to Java bean fields. It provides a
 * mechanism to automatically do this mapping. But for more sophisticated use
 * cases you can also define your own mapping (see example 'Define a custom bean
 * mapping').
 * </p>
 * <p>
 * Assume that you have following database table:
 * <table border="1" cellpadding="5" cellspacing="5">
 * <tr>
 * <td>id</td>
 * <td>lastName</td>
 * <td>firstName</td>
 * <td>balance</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>huber</td>
 * <td>sepp</td>
 * <td>5000.0</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>maier</td>
 * <td>franz</td>
 * <td>2000.0</td>
 * </tr>
 * </table>
 * </p>
 * 
 * and following Java class:
 * 
 * <pre>
 * public class AccountImpl implements Account
 * {
 *   private Long id;
 *   private String lastName;
 *   private String firstName;
 *   private Double balance = 0.0;
 * 
 * 
 *   public Long getId()
 *   {
 *     return id;
 *   }
 * 
 * 
 *   public void setId(Long id)
 *   {
 *     this.id = id;
 *   }
 * 
 * 
 *   public void setBalance(Double balance)
 *   {
 *     this.balance = balance;
 *   }
 * 
 * 
 *   public Double getBalance()
 *   {
 *     return balance;
 *   }
 * 
 * 
 *   public String getLastName()
 *   {
 *     return lastName;
 *   }
 * 
 * 
 *   public void setLastName(String lastName)
 *   {
 *     this.lastName = lastName;
 *   }
 * 
 * 
 *   public String getFirstName()
 *   {
 *     return firstName;
 *   }
 * 
 * 
 *   public void setFirstName(String firstName)
 *   {
 *     this.firstName = firstName;
 *   }
 * }
 * </pre>
 * 
 * <p>
 * Because the database column names and the Java bean field names do exactly
 * match, Jaqlib can perform full automatic mapping. Following code instantiates
 * and fills <tt>AccountImpl</tt> objects from database.
 * </p>
 * 
 * <pre>
 * List&lt;? extends Account&gt; result = Jaqlib.DB.select(AccountImpl.class)
 *     .from(accounts).asList();
 * </pre>
 * 
 * 
 * <h3>Constraining the result</h3> There are different ways how to constrain
 * the returned query result. Jaqlib provides an API for specifying WHERE
 * clauses. You can use WHERE clauses in three ways:
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
 * // get recorder object
 * Account account = Jaqlib.DB.getRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 500
 * List&lt;? extends Account&gt; results = Jaqlib.DB.select(AccountImpl.class)
 *     .from(accounts).whereCall(account.getBalance()).isGreaterThan(500.0)
 *     .asList();
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
 * // create custom WHERE condition
 * WhereCondition&lt;AccountImpl&gt; myCondition = new WhereCondition&lt;AccountImpl&gt;()
 * {
 * 
 *   public boolean evaluate(AccountImpl element)
 *   {
 *     if (element == null)
 *       return false;
 *     return element.getBalance() &gt; 500;
 *   }
 * };
 * 
 * // execute query
 * List&lt;? extends Account&gt; accountsGreater500 = Jaqlib.DB
 *     .select(AccountImpl.class).from(accounts).where(myCondition).asList();
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
 * <pre>
 * long accountId = 15;
 * AccountImpl criteria = new AccountImpl();
 * criteria.setId(accountId);
 * 
 * Account account15 = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .whereElement().isEqual(criteria).uniqueResult();
 * </pre>
 * 
 * Or you could use this mechanism to filter <tt>null</tt> elements:
 * 
 * <pre>
 * List&lt;AccountImpl&gt; notNullAccounts = Jaqlib.DB.select(AccountImpl.class)
 *     .from(accounts).where().element().isNotNull().asList();
 * </pre>
 * 
 * Or for filtering {@link Comparable} elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used
 * // for comparing two accounts
 * AccountImpl criteria = new AccountImpl();
 * criteria.setBalance(5000.0);
 * 
 * List&lt;AccountImpl&gt; result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .where().element().isSmallerThan(criteria).asList();
 * </pre>
 * 
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
 * <b>Return result as a {@link Map} or {@link Hashtable}:</b><br>
 * The key for the {@link Map} must be specified by using the method call record
 * mechanism. Again a method call on a recorder object is recorded by Jaqlib.
 * When returning the query result Jaqlib executes this recorded method on each
 * selected element and uses the result as key of the map entry.
 * </p>
 * 
 * <pre>
 * Account recorder = Jaqlib.DB.getRecorder(Account.class);
 * Map&lt;Long, AccountImpl&gt; results = Jaqlib.DB.select(AccountImpl.class)
 *     .from(accounts).asMap(recorder.getId());
 * </pre>
 * 
 * <p>
 * <b>Return result as {@link Set}:</b><br>
 * 
 * <pre>
 * Set&lt;AccountImpl&gt; notNullAccounts = Jaqlib.DB.select(AccountImpl.class)
 *     .from(accounts).whereElement().isNotNull().asSet();
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Return result as {@link List} or {@link Vector}:</b><br>
 * 
 * <pre>
 * List&lt;AccountImpl&gt; notNullAccounts = Jaqlib.DB.select(AccountImpl.class)
 *     .from(accounts).whereElement().isNotNull().asList();
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Return unique result:</b> <br>
 * Only one result is allowed to be selected by the query. If more than one
 * elements are selected then an {@link QueryResultException} is thrown. <br>
 * Note that you can use <tt>uniqueResult()</tt> or <tt>asUniqueResult()</tt> -
 * what you prefer better.
 * 
 * <pre>
 * Account recorder = Jaqlib.DB.getRecorder(Account.class);
 * Account result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .whereCall(recorder.getId()).isEqual((long) 5).asUniqueResult();
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Return only the first result:</b> <br>
 * Only the element is returned that matches the given WHERE conditions first. <br>
 * Note that you can use <tt>firstResult()</tt> or <tt>asFirstResult()</tt> -
 * what you prefer better.
 * 
 * <pre>
 * Account recorder = Jaqlib.DB.getRecorder(Account.class);
 * Account result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .whereCall(recorder.getBalance()).isGreaterThan(500.0).asFirstResult();
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <b>Return only the last result:</b> <br>
 * Only the element is returned that matches the given WHERE conditions last. <br>
 * Note that you can use <tt>lastResult()</tt> or <tt>asLastResult()</tt> - what
 * you prefer better.
 * 
 * <pre>
 * Account recorder = Jaqlib.DB.getRecorder(Account.class);
 * Account result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .whereCall(recorder.getBalance()).isGreaterThan(500.0).asLastResult();
 * </pre>
 * 
 * </p>
 * 
 * 
 * <h3>Executing a task on each element</h3>
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
 * Jaqlib.DB.select(AccountImpl.class).from(accounts).execute(task);
 * </pre>
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
 * Jaqlib.DB.select(AccountImpl.class).from(accounts).where(deptCond)
 *     .execute(task);
 * 
 * // or ...
 * List&lt;AccountImpl&gt; result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
 *     .where(deptCond).executeWithResult(task).asList();
 * </pre>
 * 
 * 
 * <h3>Executing prepared statements</h3>
 * <p>
 * If the same {@link PreparedStatement} should be used for multiple queries
 * then a PreparedStatement pool (like http://commons.apache.org/dbcp/) must be
 * used. Alternatively following code example can be used. It's important to set
 * the property <tt>AutoClosePreparedStatement</tt> to false and to close the
 * {@link DbSelectDataSource} after having issued the queries. By default the
 * <tt>AutoClosePreparedStatement</tt> is set to true. The property
 * <tt>AutoClosePreparedStatement</tt> can also be set application-wide by using
 * the {@link #DEFAULTS} objects.
 * </p>
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT WHERE fname = ? AND balance &gt; ?&quot;;
 * DbSelectDataSource accounts = new DbSelectDataSource(getJdbcDataSource(), sql);
 * accounts.setAutoClosePreparedStatement(false);
 * 
 * try
 * {
 *   List&lt;AccountImpl&gt; results1000 = Jaqlib.DB.select(AccountImpl.class)
 *       .from(accounts).using(&quot;werner&quot;, 1000).asList();
 * 
 *   List&lt;AccountImpl&gt; results2000 = Jaqlib.DB.select(AccountImpl.class)
 *       .from(accounts).using(&quot;werner&quot;, 2000).asList();
 * 
 *   List&lt;AccountImpl&gt; results3000 = Jaqlib.DB.select(AccountImpl.class)
 *       .from(accounts).using(&quot;werner&quot;, 3000).asList();
 * }
 * finally
 * {
 *   // close all used statements (in that case it's only one statement)
 *   accounts.close();
 * }
 * </pre>
 * 
 * 
 * <h3>Custom Java type handler</h3> Database column data types can be converted
 * to custom Java types with so-called {@link JavaTypeHandler}s. These handlers
 * can be registered using
 * {@link BeanMapping#registerJavaTypeHandler(JavaTypeHandler)} or
 * {@link DbDefaults#registerJavaTypeHandler(JavaTypeHandler)}.</p>
 * <p>
 * The <tt>AccountImpl</tt> class has a <tt>creditRating</tt> field with the
 * custom enumeration type <tt>CreditRating</tt>. At database this field is
 * stored as an Integer value. By using a {@link JavaTypeHandler} this Integer
 * value can be converted into the according <tt>CreditRating</tt> enumeration
 * value.
 * </p>
 * 
 * <pre>
 *     // get DbSelectDataSource and BeanMapping
 *     String sql = "SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT";
 *     DbSelectDataSource dataSource = new DbSelectDataSource(getJdbcDataSource(),
 *         sql);
 *     BeanMapping<AccountImpl> mapping = new BeanMapping<AccountImpl>(
 *         AccountImpl.class);
 * 
 *     // register custom type handler for CreditRating bean field
 *     mapping.registerJavaTypeHandler(new CreditRatingTypeHandler());
 * 
 *     // perform query
 *     Jaqlib.DB.select(mapping).from(dataSource) ...
 * </pre>
 * 
 * <pre>
 * // custom java type handler that converts Integer values from DB into
 * // CreditRating enumerations
 * public class CreditRatingTypeHandler extends AbstractJavaTypeHandler
 * {
 *   &#064;Override
 *   public Object convert(Object value)
 *   {
 *     if (value instanceof Integer)
 *       return CreditRating.rating((Integer) value);
 *     else
 *       throw handleIllegalInputValue(value, CreditRating.class);
 *   }
 * 
 * 
 *   &#064;Override
 *   protected void addSupportedTypes(List&lt;Class&lt;?&gt;&gt; types)
 *   {
 *     types.add(CreditRating.class);
 *   }
 * }
 * </pre>
 * 
 * 
 * <h3>Inserting a Java bean into a database table</h3>
 * <p>
 * <b>Using default bean mapping (bean property convention):</b>
 * </p>
 * 
 * <pre>
 * AccountImpl account = new AccountImpl();
 * // fill account with values ...
 * 
 * String tableName = &quot;ACCOUNT&quot;;
 * DbInsertDataSource dataSource = new DbInsertDataSource(getJdbcDataSource(),
 *     tableName);
 * int updateCount = Jaqlib.DB.insert(account).into(dataSource)
 *     .usingDefaultMapping();
 * </pre>
 * 
 * <p>
 * <b>Using custom bean mapping:</b>
 * </p>
 * 
 * <pre>
 * AccountImpl account = new AccountImpl();
 * // fill account with values ...
 * 
 * // create custom bean mapping
 * BeanMapping&lt;AccountImpl&gt; beanMapping = Jaqlib.DB
 *     .getDefaultBeanMapping(AccountImpl.class);
 * beanMapping.removeField(&quot;id&quot;);
 * beanMapping.getField(&quot;lastName&quot;).setSourceName(&quot;lName&quot;);
 * 
 * // insert account using the custom bean mapping and the simplified API
 * String tableName = &quot;ACCOUNT&quot;;
 * int updateCount = Jaqlib.DB.insert(account)
 *     .into(getJdbcDataSource(), tableName).using(beanMapping);
 * </pre>
 * 
 * 
 * <h3>Updating a Java bean in a database table</h3>
 * <p>
 * <b>Using default bean mapping (bean property convention):</b>
 * </p>
 * 
 * <pre>
 * // get account that already exists at database
 * AccountImpl account = getAccount();
 * 
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * DataSource ds = getJdbcDataSource();
 * DbUpdateDataSource dataSource = new DbUpdateDataSource(ds, tableName);
 * int updateCount = Jaqlib.DB.update(account).in(dataSource).where(whereClause)
 *     .usingDefaultMapping();
 * </pre>
 * 
 * <p>
 * <b>Using custom bean mapping:</b>
 * </p>
 * 
 * <pre>
 * // get account that already exists at database
 * Account account = getAccount();
 * 
 * // create custom bean mapping
 * BeanMapping&lt;Account&gt; beanMapping = new BeanMapping&lt;Account&gt;(Account.class);
 * beanMapping.removeField(&quot;id&quot;);
 * beanMapping.getField(&quot;lastName&quot;).setSourceName(&quot;lName&quot;);
 * 
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * int updateCount = Jaqlib.DB.update(account).in(getJdbcDataSource(), tableName)
 *     .where(whereClause).using(beanMapping);
 * </pre>
 * 
 * 
 * <h3>Delete records from a database table</h3>
 * <p>
 * <b>Delete specific records:</b>
 * </p>
 * 
 * <pre>
 * // get account that already exists at database
 * AccountImpl account = getAccount();
 * 
 * // delete account at database
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * DataSource ds = getJdbcDataSource();
 * DbDeleteDataSource dataSource = new DbDeleteDataSource(ds, tableName,
 *     whereClause);
 * int updateCount = Jaqlib.DB.delete().from(dataSource);
 * </pre>
 * 
 * <p>
 * <b>Delete all records:</b>
 * </p>
 * 
 * <pre>
 * // delete all records from the ACCOUNT table
 * String tableName = &quot;ACCOUNT&quot;;
 * int updateCount = Jaqlib.DB.delete().from(getJdbcDataSource(), tableName);
 * </pre>
 * 
 * @see DbDefaults
 * @see DatabaseQB
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
    return this.<T> createQuery(columnMapping).createFromClause();
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
    return this.<T> createQuery(beanMapping).createFromClause();
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


  private <T> DbQuery<T> createQuery(AbstractMapping<T> mapping)
  {
    return new DbQuery<T>(getMethodCallRecorder(), mapping);
  }

}
