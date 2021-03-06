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
package org.jaqlib.core;

import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.core.reflect.MethodCallRecorder;
import org.jaqlib.core.reflect.RecordingProxy;
import org.jaqlib.core.reflect.ThreadLocalMethodCallRecorder;
import org.jaqlib.util.Assert;

/**
 * Abstract base class for all kinds of query builders. It provides the method
 * call recording mechanism and the bean mapping functionality.
 * 
 * @author Werner Fragner
 */
public abstract class AbstractQueryBuilder
{

  private final ThreadLocalMethodCallRecorder methodCallRecorder = new ThreadLocalMethodCallRecorder();
  private final ThreadLocal<ClassLoader> classLoader = new ThreadLocal<>();


  /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getRecorder(Class)} method.
   * <p>
   * NOTE: This method sets the classloader for the <b>current</b> thread. If
   * this class is used by multiple threads then the classloader must be set for
   * each thread that uses this class. Otherwise the classloader of this class
   * is used.
   * </p>
   * 
   * @param classLoader a not null class loader.
   */
  public void setClassLoader(ClassLoader classLoader)
  {
    Assert.notNull(classLoader);
    this.classLoader.set(classLoader);
  }


  private ClassLoader getClassLoader()
  {
    ClassLoader classLoader = this.classLoader.get();
    if (classLoader == null)
    {
      return getClass().getClassLoader();
    }
    else
    {
      return classLoader;
    }
  }


  /**
   * @return the method call recorder for the current thread. Never returns
   *         null.
   */
  protected MethodCallRecorder getMethodCallRecorder()
  {
    return methodCallRecorder;
  }


  /**
   * @param <T> the type of the result element.
   * @param recorder a not null class of the recorder object.
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public <T> T getRecorder(Class<T> recorder)
  {
    RecordingProxy<T> proxy = new RecordingProxy<>(getClassLoader());
    methodCallRecorder.set(proxy.getMethodCallRecorder());
    return proxy.getProxy(recorder);
  }


  /**
   * See {@link BeanMapping#build(BeanMappingStrategy, Class)}.
   */
  public <T> BeanMapping<T> getDefaultBeanMapping(Class<? extends T> beanClass)
  {
    return getBeanMapping(Defaults.getBeanMappingStrategy(), beanClass);
  }


  /**
   * See {@link BeanMapping#build(Class)}.
   */
  public <T> BeanMapping<T> getBeanMapping(BeanMappingStrategy mappingStrategy,
      Class<? extends T> beanClass)
  {
    return BeanMapping.build(mappingStrategy, beanClass);
  }

}
