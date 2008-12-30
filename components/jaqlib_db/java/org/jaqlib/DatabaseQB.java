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

import org.jaqlib.db.BeanDbSelectResult;
import org.jaqlib.db.Column;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.query.FromClause;
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.query.db.DatabaseQBProperties;


/**
 * Helper class with static methods for database query support (see
 * {@link DatabaseQueryBuilder} for further information).
 * 
 * <p>
 * This class provides static helper methods to easily execute queries against
 * databases via JDBC. <br>
 * Examples are given here: {@link DatabaseQueryBuilder}.
 * </p>
 * This class is thread-safe.
 * 
 * @see DatabaseQueryBuilder
 * @author Werner Fragner
 */
public class DatabaseQB
{

  /**
   * This class is not intended to be instantiated.
   */
  private DatabaseQB()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Default properties for {@link DatabaseQueryBuilder}.
   */
  private static final DatabaseQBProperties PROPERTIES = new DatabaseQBProperties();

  /**
   * Singleton instance.
   */
  private static final DatabaseQueryBuilder QUERYBUILDER = new DatabaseQueryBuilder(
      PROPERTIES);


  /**
   * Enables/disables strict checking if a column in the desired result does not
   * exist in the SELECT statement. If strict column check is enabled then an
   * exception is thrown if a column does exist in the SELECT statement. If
   * strict column check is disabled (DEFAULT) then an INFO log message is
   * issued and the column is ignored. If these INFO messages should not be
   * issued anymore then the JDK logger for
   * 'org.jaqlib.query.db.AbstractJaqLibOrMapper' must be disabled (see <a href=
   * "http//java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html">Java
   * Logging</a>).
   * 
   * @param strictColumnCheck
   */
  public void setStrictColumnCheck(boolean strictColumnCheck)
  {
    PROPERTIES.setStrictColumnCheck(strictColumnCheck);
  }


  /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getMethodCallRecorder(Class)} method.
   * 
   * @param classLoader a not null class loader.
   */
  public static void setClassLoader(ClassLoader classLoader)
  {
    QUERYBUILDER.setClassLoader(classLoader);
  }


  /**
   * @param <T> the type of the result element(s).
   * @param resultElementClass a not null class of the result element.
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public static <T> T getMethodCallRecorder(Class<T> resultElementClass)
  {
    return QUERYBUILDER.getMethodCallRecorder(resultElementClass);
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
  public static <T> FromClause<T, DbSelectDataSource> select(Column<T> column)
  {
    return QUERYBUILDER.select(column);
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
  public static <T> FromClause<T, DbSelectDataSource> select(Class<T> beanClass)
  {
    return QUERYBUILDER.select(beanClass);
  }


  public static <T> FromClause<T, DbSelectDataSource> select(
      BeanDbSelectResult<T> resultDefinition)
  {
    return QUERYBUILDER.select(resultDefinition);
  }

}
