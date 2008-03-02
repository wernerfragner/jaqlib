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

import org.jaqlib.query.FromClause;
import org.jaqlib.query.Query;
import org.jaqlib.query.QueryBuilder;
import org.jaqlib.query.QueryBuilderFactory;
import org.jaqlib.query.iterable.IterableQueryBuilderFactory;
import org.jaqlib.reflect.JaqlibInvocationRecorder;
import org.jaqlib.reflect.JaqlibProxy;
import org.jaqlib.util.Assert;


/**
 * QB ... QueryBuilder
 * <p>
 * Main entry point of JaQLib. It provides methods for building queries and
 * adapting the query building process.
 * 
 * @author Werner Fragner
 */
public class QB
{

  protected static final ThreadLocal<JaqlibInvocationRecorder> invocationRecorder = new ThreadLocal<JaqlibInvocationRecorder>();

  protected static final ThreadLocal<ClassLoader> classLoader = new ThreadLocal<ClassLoader>();
  protected static final ThreadLocal<QueryBuilderFactory> queryBuilderFactory = new ThreadLocal<QueryBuilderFactory>();


  static
  {
    // set default class loader
    classLoader.set(QB.class.getClassLoader());
    // set default query builder factory
    queryBuilderFactory.set(new IterableQueryBuilderFactory());
  }


  /**
   * Sets a user-defined classloader that is used when creating proxy classes.
   * 
   * @param classLoader
   */
  public static void setClassLoader(ClassLoader classLoader)
  {
    Assert.notNull(classLoader);
    QB.classLoader.set(classLoader);
  }


  /**
   * Sets a user-defined query builder factory. By default the
   * {@link org.jaqlib.query.iterable.IterableQueryBuilderFactory} is used by
   * this class.
   * 
   * @param queryBuilderFactory
   */
  public static void setQueryBuilderFactory(
      QueryBuilderFactory queryBuilderFactory)
  {
    Assert.notNull(queryBuilderFactory);
    QB.queryBuilderFactory.set(queryBuilderFactory);
  }


  /**
   * @param <T>
   * @param resultItemClass
   * @return a proxy object that records all method invocations. These
   *         invocations can be used when evaluating the query.
   */
  public static <T> T getMethodCallRecorder(Class<T> resultItemClass)
  {
    JaqlibProxy<T> proxy = new JaqlibProxy<T>(classLoader.get());
    invocationRecorder.set(proxy.getInvocationRecorder());
    return proxy.getProxy(resultItemClass);
  }


  public static <T, DataSourceType> FromClause<T, DataSourceType> select(
      Class<T> resultItemClass)
  {
    QueryBuilder<T, DataSourceType> queryBuilder = getQueryBuilder();
    Query<T, DataSourceType> query = queryBuilder.createQuery();
    return query.createFromClause(resultItemClass);
  }


  public static <T, DataSourceType> FromClause<T, DataSourceType> select(
      Class<T>... resultItemClasses)
  {
    QueryBuilder<T, DataSourceType> queryBuilder = getQueryBuilder();
    Query<T, DataSourceType> query = queryBuilder.createQuery();
    return query.createFromClause(resultItemClasses);
  }


  protected static <T, DataSourceType> QueryBuilder<T, DataSourceType> getQueryBuilder()
  {
    return getQueryBuilderFactory().getQueryBuilder(invocationRecorder.get());
  }


  protected static QueryBuilderFactory getQueryBuilderFactory()
  {
    return queryBuilderFactory.get();
  }


}
