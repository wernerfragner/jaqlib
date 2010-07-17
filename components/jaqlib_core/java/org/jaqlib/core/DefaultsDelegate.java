package org.jaqlib.core;

import org.jaqlib.Defaults;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.core.bean.JavaTypeHandlerRegistry;
import org.jaqlib.core.bean.BeanMappingStrategy;

/**
 * Helper class that makes the static methods of {@link Defaults} into methods
 * of an object.
 * 
 * @author Werner Fragner
 */
public abstract class DefaultsDelegate
{

  /**
   * See {@link Defaults#getJavaTypeHandlerRegistry()}.
   */
  public BeanFactory getBeanFactory()
  {
    return Defaults.getBeanFactory();
  }


  /**
   * See {@link Defaults#setBeanFactory(BeanFactory)}.
   */
  public void setBeanFactory(BeanFactory beanFactory)
  {
    Defaults.setBeanFactory(beanFactory);
  }


  /**
   * See {@link Defaults#getMappingStrategy()}.
   */
  public BeanMappingStrategy getMappingStrategy()
  {
    return Defaults.getMappingStrategy();
  }


  /**
   * See {@link Defaults#setMappingStrategy(BeanMappingStrategy)}.
   */
  public void setMappingStrategy(BeanMappingStrategy strategy)
  {
    Defaults.setMappingStrategy(strategy);
  }


  /**
   * See {@link Defaults#getJavaTypeHandlerRegistry()}.
   */
  public JavaTypeHandlerRegistry getJavaTypeHandlerRegistry()
  {
    return Defaults.getJavaTypeHandlerRegistry();
  }


  /**
   * See {@link Defaults#setJavaTypeHandlerRegistry(JavaTypeHandlerRegistry)}.
   */
  public void setJavaTypeHandlerRegistry(JavaTypeHandlerRegistry registry)
  {
    Defaults.setJavaTypeHandlerRegistry(registry);
  }


  /**
   * See {@link Defaults#registerJavaTypeHandler(JavaTypeHandler)}.
   */
  public void registerJavaTypeHandler(JavaTypeHandler typeHandler)
  {
    Defaults.registerJavaTypeHandler(typeHandler);
  }


  /**
   * See {@link Defaults#setStrictFieldCheck(boolean)}.
   */
  public void setStrictFieldCheck(boolean strictFieldCheck)
  {
    Defaults.setStrictFieldCheck(strictFieldCheck);
  }


  /**
   * See {@link Defaults#getStrictFieldCheck()}.
   */
  public boolean getStrictFieldCheck()
  {
    return Defaults.getStrictFieldCheck();
  }


  /**
   * See {@link Defaults#reset()}.
   */
  public void reset()
  {
    Defaults.reset();
  }

}
