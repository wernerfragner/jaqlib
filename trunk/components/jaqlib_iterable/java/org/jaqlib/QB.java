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
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.query.iterable.IterableQuery;
import org.jaqlib.reflect.MethodCallRecorder;
import org.jaqlib.reflect.RecordingProxy;
import org.jaqlib.util.Assert;


/**
 * QB stands for QueryBuilder (see {@link IterableQueryBuilder} for further
 * information).
 * 
 * @see IterableQueryBuilder
 * @author Werner Fragner
 */
public class QB
{

  private static final ThreadLocal<MethodCallRecorder> methodCallRecorder = new ThreadLocal<MethodCallRecorder>();
  private static final ThreadLocal<ClassLoader> classLoader = new ThreadLocal<ClassLoader>();


  /**
   * Initializes this class with the a default class loader.
   */
  static
  {
    // set default class loader
    classLoader.set(QB.class.getClassLoader());
  }


  /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getMethodCallRecorder(Class)} method.
   * 
   * @param classLoader a not null class loader.
   */
  public static void setClassLoader(ClassLoader classLoader)
  {
    Assert.notNull(classLoader);
    QB.classLoader.set(classLoader);
  }


  /**
   * @param <T> the type of the result element(s).
   * @param resultElementClass a not null class of the result element(s).
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public static <T> T getMethodCallRecorder(Class<T> resultElementClass)
  {
    RecordingProxy<T> proxy = new RecordingProxy<T>(classLoader.get());
    methodCallRecorder.set(proxy.getInvocationRecorder());
    return proxy.getProxy(resultElementClass);
  }


  /**
   * Selects a certain set of objects in a given collection. The collection that
   * should be used must be specified in the returned {@link FromClause}. The
   * {@link FromClause} hereby returns a {@link WhereClause} that can be used to
   * specify an arbitrary WHERE condition. This WHERE condition supports AND and
   * OR connectors, the evaluation of user-defined {@link WhereCondition}s and
   * user-defined {@link ReflectiveWhereCondition}s.
   * 
   * @param <T> the collection element type.
   * @param resultElementClass the class of the result elements. This class is
   *          only necessary for type safety.
   * @return the FROM clause to specify the source collection for the query.
   */
  public static <T> FromClause<T, Iterable<T>> select(
      Class<T> resultElementClass)
  {
    return QB.<T> createQuery().createFromClause(resultElementClass);
  }


  /**
   * This method has basically the same functionality as {@link #select(Class)}.
   * But this method is not type safe regarding the returned result.
   * 
   * @return the FROM clause to specify the source collection of the query.
   */
  public static FromClause<Object, Iterable<Object>> select()
  {
    return QB.createQuery().createFromClause(new Class[0]);
  }


  private static <T> IterableQuery<T> createQuery()
  {
    return getQueryBuilder().createQuery();
  }


  /**
   * @return a new {@link IterableQueryBuilder} instance.
   */
  private static IterableQueryBuilder getQueryBuilder()
  {
    return new IterableQueryBuilder(methodCallRecorder.get());
  }

}
