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
import org.jaqlib.db.BeanFactory;
import org.jaqlib.db.BeanMapping;
import org.jaqlib.db.ColumnMapping;
import org.jaqlib.db.DatabaseQuery;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.DeleteFromClause;
import org.jaqlib.db.InClause;
import org.jaqlib.db.IntoClause;
import org.jaqlib.db.Using;
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
 * or if multiple SQL SELECT statements should be executed against the same JDBC
 * {@link DataSource}:
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * Database db = new Database(getJdbcDataSource());
 * DbSelectDataSource accounts = db.getSelectDataSource(sql);
 * </pre>
 * 
 * <i>Method call recording mechanism:</i>
 * 
 * <pre>
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = DatabaseQB.getRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 5000
 * List&lt;AccountImpl&gt; results = DatabaseQB.select(AccountImpl.class).from(accounts)
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
 * List&lt;AccountImpl&gt; highRiskAccounts = DatabaseQB.select(AccountImpl.class).from(accounts)
 *     .where(deptCondition).and(ratingCondition).asList();
 * </pre>
 * 
 * <i>Filtering null elements:</i>
 * 
 * <pre>
 * List&lt;AccountImpl&gt; notNullAccounts = DatabaseQB.select(AccountImpl.class).from(
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
 * List&lt;AccountImpl&gt; result = DatabaseQB.select(AccountImpl.class).from(accounts)
 *     .where().element().isSmallerThan(spec).asList();
 * </pre>
 * 
 * <i>Map as result:</i>
 * 
 * <pre>
 * Account account = DatabaseQB.getRecorder(Account.class);
 * Map&lt;Long, AccountImpl&gt; results = DatabaseQB.select(AccountImpl.class).from(
 *     accounts).asMap(account.getId());
 * </pre>
 * 
 * </p>
 * <p>
 * <i>Custom Java type handler:</i>
 * <p>
 * Database column data types can be converted to custom Java types with
 * so-called {@link JavaTypeHandler}s. These handlers can be registered with
 * {@link BeanMapping#registerJavaTypeHandler(Class, JavaTypeHandler)} .
 * </p>
 * <p>
 * The <tt>AccountImpl</tt> class has a <tt>creditRating</tt> field with the
 * custom enumeration type <tt>CreditRating</tt>. At database this field is
 * stored as an Integer value. By using a {@link JavaTypeHandler} this Integer
 * value is converted into the according <tt>CreditRating</tt> enumeration
 * value.
 * </p>
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
 * <i>Inserting a Java bean into a database table:</i>
 * 
 * <pre>
 * AccountImpl account = new AccountImpl();
 * // fill account with values ...
 * 
 * String tableName = &quot;ACCOUNT&quot;;
 * DbInsertDataSource ds = Database
 *     .getInsertDataSource(getDataSource(), tableName);
 * int updateCount = DatabaseQB.insert(account).into(ds).usingDefaultMapping();
 * </pre>
 * 
 * <i>Inserting a Java bean into a database table using a custom bean
 * mapping:</i>
 * 
 * <pre>
 * AccountImpl account = new AccountImpl();
 * // fill account with values ...
 * 
 * // create custom bean mapping
 * BeanMapping&lt;AccountImpl&gt; beanMapping = Database
 *     .getDefaultBeanMapping(AccountImpl.class);
 * beanMapping.removeChildColumn(&quot;id&quot;);
 * beanMapping.getChildColumn(&quot;lastName&quot;).setColumnName(&quot;lName&quot;);
 * 
 * // insert account using the custom bean mapping
 * String tableName = &quot;ACCOUNT&quot;;
 * DbInsertDataSource ds = Database
 *     .getInsertDataSource(getDataSource(), tableName);
 * int updateCount = DatabaseQB.insert(account).into(ds).using(beanMapping);
 * </pre>
 * 
 * <i>Updating a Java bean in a database table:</i>
 * 
 * <pre>
 * // get account that already exists at database 
 * AccountImpl account = getAccount();
 * 
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * DbUpdateDataSource ds = Database.getUpdateDataSource(getDataSource(),
 *     tableName, whereClause);
 * int updateCount = DatabaseQB.update(account).in(ds).usingDefaultMapping();
 * </pre>
 * 
 * <i>Updating a Java bean in a database table using a custom bean mapping:</i>
 * 
 * <pre>
 * // get account that already exists at database 
 * AccountImpl account = getAccount();
 * 
 * // create custom bean mapping
 * BeanMapping&lt;AccountImpl&gt; beanMapping = Database
 *     .getDefaultBeanMapping(AccountImpl.class);
 * beanMapping.removeChildColumn(&quot;id&quot;);
 * beanMapping.getChildColumn(&quot;lastName&quot;).setColumnName(&quot;lName&quot;);
 * 
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * DbUpdateDataSource ds = Database.getUpdateDataSource(getDataSource(),
 *     tableName, whereClause);
 * int updateCount = DatabaseQB.update(account).in(ds).using(beanMapping);
 * </pre>
 * 
 * <i>Delete specific records from a database table:</i>
 * 
 * <pre>
 * // get account that already exists at database 
 * AccountImpl account = getAccount();
 * 
 * // delete account at database
 * String whereClause = &quot;id = &quot; + account.getId();
 * String tableName = &quot;ACCOUNT&quot;;
 * DbDeleteDataSource ds = Database.getDeleteDataSource(getDataSource(),
 *     tableName, whereClause);
 * int updateCount = DatabaseQB.delete().from(ds);
 * </pre>
 * 
 * <i>Delete all records from a database table:</i>
 * 
 * <pre>
 * // delete all records from the ACCOUNT table 
 * String tableName = &quot;ACCOUNT&quot;;
 * DbDeleteDataSource ds = Database
 *     .getDeleteDataSource(getDataSource(), tableName);
 * int updateCount = DatabaseQB.delete().from(ds);
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
   *          constructor. If the bean does not provide one a custom
   *          {@link BeanFactory} must be registered at the {@link BeanMapping}.
   *          This {@link BeanMapping} can be obtained from {@link Database} and
   *          can be used with {@link #select(BeanMapping)}.
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
   * mapping can defined with a {@link BeanMapping} instance. For building these
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
