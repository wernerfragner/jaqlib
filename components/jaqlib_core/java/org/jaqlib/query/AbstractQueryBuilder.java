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
package org.jaqlib.query;

import org.jaqlib.reflect.MethodCallRecorder;
import org.jaqlib.reflect.NullMethodCallRecorder;
import org.jaqlib.reflect.RecordingProxy;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public abstract class AbstractQueryBuilder
{

  private final ThreadLocal<MethodCallRecorder> methodCallRecorder = new ThreadLocal<MethodCallRecorder>();
  private final ThreadLocal<ClassLoader> classLoader = new ThreadLocal<ClassLoader>();


  /**
   * Initializes this class with the a default class loader.
   */
  public AbstractQueryBuilder()
  {
    classLoader.set(getClass().getClassLoader());
    methodCallRecorder.set(new NullMethodCallRecorder());
  }


  /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getMethodCallRecorder(Class)} method.
   * 
   * @param classLoader a not null class loader.
   */
  public void setClassLoader(ClassLoader classLoader)
  {
    Assert.notNull(classLoader);
    this.classLoader.set(classLoader);
  }


  /**
   * @return the method call recorder for the current thread. Can return null if
   *         no method call recorder has been created previously.
   */
  protected MethodCallRecorder getMethodCallRecorder()
  {
    return methodCallRecorder.get();
  }


  /**
   * @param <T> the type of the result element(s).
   * @param resultElementClass a not null class of the result element(s).
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public <T> T getMethodCallRecorder(Class<T> resultElementClass)
  {
    RecordingProxy<T> proxy = new RecordingProxy<T>(classLoader.get());
    methodCallRecorder.set(proxy.getInvocationRecorder());
    return proxy.getProxy(resultElementClass);
  }


}
