package org.jaqlib.db;

/**
 * Abstracts the creation of bean instances.
 * 
 * @author Werner Fragner
 */
public interface BeanFactory
{

  /**
   * @param <T> the type of the bean.
   * @param beanClass a not null bean class.
   * @return a new instance of the given class.
   */
  <T> T newInstance(Class<T> beanClass);

}
