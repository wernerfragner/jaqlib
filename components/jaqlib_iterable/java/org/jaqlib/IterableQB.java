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

import org.jaqlib.core.WhereClause;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.MappingStrategy;
import org.jaqlib.iterable.FromClause;


/**
 * Helper class with static methods for {@link java.lang.Iterable} support (see
 * {@link IterableQueryBuilder} for further information).
 * 
 * <p>
 * This class provides static helper methods to easily execute queries against
 * data sources that implement the {@link Iterable} interface. <br>
 * Examples are given here: {@link IterableQueryBuilder}.
 * </p>
 * This class is thread-safe.
 * 
 * @see IterableQueryBuilder
 * @see IterableDefaults
 * @author Werner Fragner
 */
public class IterableQB
{

  /**
   * This class is not intended to be instantiated.
   */
  private IterableQB()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Holds singleton instances per thread.
   */
  private static final ThreadLocal<IterableQueryBuilder> QUERYBUILDER = new ThreadLocal<IterableQueryBuilder>();


  /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getRecorder(Class)} method.
   * 
   * @param classLoader a not null class loader.
   */
  public static void setClassLoader(ClassLoader classLoader)
  {
    getQueryBuilder().setClassLoader(classLoader);
  }


  /**
   * @param <T> the type of the result element(s).
   * @param resultElementClass a not null class of the result element.
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public static <T> T getRecorder(Class<T> resultElementClass)
  {
    return getQueryBuilder().getRecorder(resultElementClass);
  }


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
  public static FromClause select()
  {
    return getQueryBuilder().select();
  }


  /**
   * Shortcut method for <tt>select().from()</tt>.
   * 
   * @param iterable a not null Iterable for the query.
   * @return a where clause for defining the query conditions.
   */
  public static <T> WhereClause<T, Iterable<T>> selectFrom(Iterable<T> iterable)
  {
    return getQueryBuilder().selectFrom(iterable);
  }


  /**
   * See {@link BeanMapping#build(Class)}.
   */
  public static <T> BeanMapping<T> getDefaultBeanMapping(
      Class<? extends T> beanClass)
  {
    return getQueryBuilder().getDefaultBeanMapping(beanClass);
  }


  /**
   * See {@link BeanMapping#build(MappingStrategy, Class)}.
   */
  public static <T> BeanMapping<T> getBeanMapping(
      MappingStrategy mappingStrategy, Class<? extends T> beanClass)
  {
    return getQueryBuilder().getBeanMapping(mappingStrategy, beanClass);
  }


  /**
   * See return tag.
   * 
   * @return the query builder instance for the current thread.
   */
  static IterableQueryBuilder getQueryBuilder()
  {
    IterableQueryBuilder queryBuilder = QUERYBUILDER.get();
    if (queryBuilder == null)
    {
      queryBuilder = new IterableQueryBuilder();
      QUERYBUILDER.set(queryBuilder);
    }
    return queryBuilder;
  }

}
