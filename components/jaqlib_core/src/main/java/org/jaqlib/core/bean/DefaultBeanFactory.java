package org.jaqlib.core.bean;

import org.jaqlib.util.ReflectionUtil;

/**
 * <p>
 * Implementation of the {@link BeanFactory} interface that creates new bean
 * instances using the default constructor. If no default constructor exists
 * then a {@link RuntimeException} is thrown.
 * </p>
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * @author Werner Fragner
 */
public class DefaultBeanFactory implements BeanFactory
{

  public <T> T newInstance(Class<T> beanClass)
  {
    return ReflectionUtil.newInstance(beanClass);
  }

}
