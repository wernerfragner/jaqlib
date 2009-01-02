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

import org.jaqlib.core.FromClause;
import org.jaqlib.core.WhereClause;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.db.BeanMapping;
import org.jaqlib.db.ColumnMapping;
import org.jaqlib.db.DbSelectDataSource;


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
 * @see Database
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
   * Singleton instance.
   */
  private static final DatabaseQueryBuilder QUERYBUILDER = new DatabaseQueryBuilder();


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
   * @param <T> the type of the result bean.
   * @param beanClass a not null bean class where to store the result of the
   *          query.
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public static <T> T getMethodCallRecorder(Class<T> beanClass)
  {
    return QUERYBUILDER.getMethodCallRecorder(beanClass);
  }


  /**
   * <p>
   * Selects one column of a given database SELECT statement. The SELECT
   * statement that should be used must be specified in the returned
   * {@link FromClause}. The {@link FromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of custom {@link WhereCondition}s and custom conditions using a
   * method call recording mechanism (see examples and
   * {@link #getMethodCallRecorder(Class)} for further details).
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
  public static <T> FromClause<T, DbSelectDataSource> select(
      ColumnMapping<T> column)
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
   * evaluation of custom {@link WhereCondition}s and custom conditions using a
   * method call recording mechanism (see examples and
   * {@link #getMethodCallRecorder(Class)} for further details).
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
  public static <T> FromClause<T, DbSelectDataSource> select(Class<T> beanClass)
  {
    return QUERYBUILDER.select(beanClass);
  }


  /**
   * This method basically provides the same functionality as
   * {@link #select(Class)}. But it gives more flexibility in defining the
   * mapping between SELECT statement results to Java bean instance fields. This
   * mapping can defined with a {@link BeanMapping} instance. For build these
   * instances see {@link Database}.
   * 
   * @param <T> the result bean type.
   * @param resultDefinition a bean definition that holds information how to map
   *          the result of the SELECT statement to a Java bean.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public static <T> FromClause<T, DbSelectDataSource> select(
      BeanMapping<T> resultDefinition)
  {
    return QUERYBUILDER.select(resultDefinition);
  }

}
