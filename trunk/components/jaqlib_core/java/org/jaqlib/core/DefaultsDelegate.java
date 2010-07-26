package org.jaqlib.core;

import java.util.logging.Handler;
import java.util.logging.Level;

import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.core.bean.CollectionFactory;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.core.bean.JavaTypeHandlerRegistry;

/**
 * Helper class that makes the static methods of {@link Defaults} into methods
 * of an object.
 * 
 * @author Werner Fragner
 */
public class DefaultsDelegate
{

  /**
   * See {@link Defaults#enableConsoleLogging()}.
   */
  public void enableConsoleLogging()
  {
    Defaults.enableConsoleLogging();
  }


  /**
   * See {@link Defaults#disableConsoleLogging()}.
   */
  public void disableConsoleLogging()
  {
    Defaults.disableConsoleLogging();
  }


  /**
   * See {@link Defaults#registerLogHandler(Handler, Level)}.
   */
  public void registerLogHandler(Handler handler, Level level)
  {
    Defaults.registerLogHandler(handler, level);
  }


  /**
   * See {@link Defaults#unregisterLogHandler(Handler)}.
   */
  public void unregisterLogHandler(Handler handler)
  {
    Defaults.unregisterLogHandler(handler);
  }


  /**
   * See {@link Defaults#getBeanFactory()}.
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
   * See {@link Defaults#getCollectionFactory()}.
   */
  public CollectionFactory getCollectionFactory()
  {
    return Defaults.getCollectionFactory();
  }


  /**
   * See {@link Defaults#setCollectionFactory(CollectionFactory)}.
   */
  public void setcollectionFactory(CollectionFactory collectionFactory)
  {
    Defaults.setCollectionFactory(collectionFactory);
  }


  /**
   * See {@link Defaults#getBeanMappingStrategy()}.
   */
  public BeanMappingStrategy getBeanMappingStrategy()
  {
    return Defaults.getBeanMappingStrategy();
  }


  /**
   * See {@link Defaults#setBeanMappingStrategy(BeanMappingStrategy)}.
   */
  public void setBeanMappingStrategy(BeanMappingStrategy strategy)
  {
    Defaults.setBeanMappingStrategy(strategy);
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
