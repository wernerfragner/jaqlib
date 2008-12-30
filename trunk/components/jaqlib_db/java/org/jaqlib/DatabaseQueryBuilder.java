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

import org.jaqlib.db.BeanDbSelectResult;
import org.jaqlib.db.Column;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.PrimitiveDbSelectResult;
import org.jaqlib.query.AbstractQueryBuilder;
import org.jaqlib.query.FromClause;
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.query.db.DatabaseQBProperties;
import org.jaqlib.query.db.DatabaseQuery;
import org.jaqlib.util.Assert;

/**
 * <p>
 * The main entry point of JaQLib for database query support. It provides
 * methods for building queries ( {@link #select(PrimitiveDbSelectResult)},
 * {@link #select(BeanDbSelectResult) }) and adapting the query building process
 * ( {@link #setClassLoader(ClassLoader)} ).</br> The Method
 * {@link #getMethodCallRecorder(Class)} can be used to define a WHERE condition
 * where a return value of method call is compared to an other value (see also
 * the first example below).
 * </p>
 * <p>
 * <b>Usage examples:</b><br>
 * All examples use following statements to define the database connection and
 * the SQL SELECT statement that should act as data source for the accounts.<br>
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * DbSelectDataSource accounts = Database.getSelectDataSource(getJdbcDataSource(),
 *     sql);
 * </pre>
 * 
 * or (if multiple SQL SELECT statements should be execute against the same JDBC
 * {@link DataSource}).
 * 
 * <pre>
 * String sql = &quot;SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * Database db = new Database(getJdbcDataSource());
 * DbSelectDataSource accounts = db.getSelectDataSource(sql);
 * </pre>
 * 
 * <i>Example with method call recording:</i>
 * 
 * <pre>
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = DatabaseQB.getMethodCallRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 5000
 * List&lt;AccountImpl&gt; results = DatabaseQB.select(AccountImpl.class).from(accounts)
 *     .where(account.getBalance()).isGreaterThan(5000).asList();
 * </pre>
 * 
 * <i>Example with user-defined WHERE conditions:</i>
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
 *<pre>
 * Account account = QB.getMethodCallRecorder(Account.class);
 * Map&lt;Long, AccountImpl&gt; results = DatabaseQB.select(AccountImpl.class).from(
 *     accounts).asMap(account.getId());
 * </pre>
 * 
 * </p>
 * 
 * @author Werner Fragner
 */
public class DatabaseQueryBuilder extends AbstractQueryBuilder
{

  private final DatabaseQBProperties properties;


  /**
   * Default constructor that initializes this class with the default
   * properties.
   */
  public DatabaseQueryBuilder()
  {
    this(new DatabaseQBProperties());
  }


  /**
   * Constructor for specifying user-defined properties.
   * 
   * @param properties a not null properties object.
   */
  public DatabaseQueryBuilder(DatabaseQBProperties properties)
  {
    this.properties = Assert.notNull(properties);
  }


  private <T> FromClause<T, DbSelectDataSource> select(
      PrimitiveDbSelectResult<T> resultDefinition)
  {
    return this.<T> createQuery().createFromClause(resultDefinition);
  }


  public <T> FromClause<T, DbSelectDataSource> select(
      BeanDbSelectResult<T> resultDefinition)
  {
    return this.<T> createQuery().createFromClause(resultDefinition);
  }


  /**
   * <p>
   * Selects one column of a given database SELECT statement. The SELECT
   * statement that should be used must be specified in the returned
   * {@link FromClause}. The {@link FromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of user-defined {@link WhereCondition}s and user-defined
   * {@link ReflectiveWhereCondition}s.
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed at database-side but at Java
   * side. Avoid executing SELECT statements with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result element type.
   * @param column an object defining the desired column.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> FromClause<T, DbSelectDataSource> select(Column<T> column)
  {
    PrimitiveDbSelectResult<T> primitiveResult = new PrimitiveDbSelectResult<T>();
    primitiveResult.setColumnIndex(column.getColumnIndex());
    primitiveResult.setColumnName(column.getColumnName());
    return select(primitiveResult);
  }


  /**
   * <p>
   * Uses a given database SELECT statement to fill a user-defined Java bean.
   * The SELECT statement that should be used must be specified in the returned
   * {@link FromClause}. The {@link FromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of user-defined {@link WhereCondition}s and user-defined
   * conditions using a method call recording mechanism (see examples and
   * {@link #getMethodCallRecorder(Class)} for further details).
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed at database-side but at Java
   * side. Avoid executing SELECT statements with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result element type.
   * @param beanClass the desired result bean. This bean must provide a default
   *          constructor (otherwise a {@link RuntimeException} is thrown).
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> FromClause<T, DbSelectDataSource> select(Class<T> beanClass)
  {
    BeanDbSelectResult<T> beanResult = Database.getDefaultBeanResult(beanClass);
    return select(beanResult);
  }


  private <T> DatabaseQuery<T> createQuery()
  {
    return new DatabaseQuery<T>(getMethodCallRecorder(), properties);
  }

}
