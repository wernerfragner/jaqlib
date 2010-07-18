package org.jaqlib.core.bean;

/**
 * Abstracts the creation of bean instances. This interface can be used if you
 * want to take control of the bean instantiation by Jaqlib. This could be
 * useful, for example, if you want to initialize all instantiated beans with
 * certain values.
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
