package org.jaqlib.db;

import org.jaqlib.util.ReflectionUtil;

/**
 * <p>
 * Implementation of the {@link BeanFactory} interface that creates new bean
 * instances using the default constructor. If no default constructor exists
 * then a {@link RuntimeException} is thrown.
 * </p>
 * <p>
 * This class is implemented as a singleton and cannot be instantiated.<br>
 * This class is thread-safe.
 * </p>
 * 
 * @author Werner Fragner
 */
public class DefaultBeanFactory implements BeanFactory
{

  public static final BeanFactory INSTANCE = new DefaultBeanFactory();


  private DefaultBeanFactory()
  {
    throw new UnsupportedOperationException();
  }


  public <T> T newInstance(Class<T> beanClass)
  {
    return ReflectionUtil.newInstance(beanClass);
  }

}
