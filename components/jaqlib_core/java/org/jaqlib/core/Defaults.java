package org.jaqlib.core;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaqlib.core.bean.BeanConventionMappingStrategy;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.core.bean.DefaultBeanFactory;
import org.jaqlib.core.bean.DefaultJavaTypeHandlerRegistry;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.core.bean.JavaTypeHandlerRegistry;
import org.jaqlib.util.Assert;
import org.jaqlib.util.LogUtil;

/**
 * <p>
 * Normally this class should not be used. Use the domain specific defaults
 * classes instead:
 * <ul>
 * <li>IterableDefaults</li>
 * <li>DbDefaults</li>
 * <li>XmlDefaults</li>
 * </ul>
 * </p>
 * <p>
 * Static helper class that holds default infrastructure component instances and
 * application-wide properties.
 * </p>
 * <p>
 * <b>NOTE: Changes to these components/properties have an effect on the entire
 * application. Use with care!</b>
 * </p>
 * 
 * @author Werner Fragner
 */
public class Defaults
{

  /**
   * This class is not intended to be instantiated.
   */
  private Defaults()
  {
    throw new UnsupportedOperationException();
  }


  private static BeanFactory beanFactory;
  private static BeanMappingStrategy beanMappingStrategy;
  private static JavaTypeHandlerRegistry javaTypeHandlerRegistry;
  private static boolean strictFieldCheck;


  /**
   * Resets all defaults to their initial values.
   */
  static
  {
    reset();
  }


  /**
   * Resets all defaults to their initial values.
   */
  public static void reset()
  {
    beanFactory = new DefaultBeanFactory();
    beanMappingStrategy = new BeanConventionMappingStrategy();
    javaTypeHandlerRegistry = new DefaultJavaTypeHandlerRegistry();
    strictFieldCheck = false;
  }


  /**
   * <p>
   * Enables logging of all JaQLib messages to the console. This can be useful,
   * for example, for debugging.
   * </p>
   * <p>
   * By default logging to the console is disabled.
   * </p>
   * <p>
   * If you want to redirect the JaqLib log output to your own logging handlers
   * you can use {@link #registerLogHandler(Handler, Level)}.
   * </p>
   */
  public static void enableConsoleLogging()
  {
    LogUtil.enableConsoleLogging();
  }


  /**
   * <p>
   * Disables logging of all JaQLib messages to the console.
   * </p>
   * By default logging to the console is disabled.
   */
  public static void disableConsoleLogging()
  {
    LogUtil.disableConsoleLogging();
  }


  /**
   * Registers the given log handler using the given log level.
   * 
   * @param handler the custom log handler.
   * @param level the desired log level. Message levels lower then this value
   *          will be discarded.
   */
  public static void registerLogHandler(Handler handler, Level level)
  {
    Logger logger = LogUtil.getJaqlibLogger();
    logger.setLevel(level);
    logger.addHandler(handler);
  }


  /**
   * Unregisters the given log handler. If the given handler has not been
   * registered before then no action is performed.
   * 
   * @param handler the handler to remove; may me null.
   */
  public static void unregisterLogHandler(Handler handler)
  {
    Logger logger = LogUtil.getJaqlibLogger();
    logger.removeHandler(handler);
  }


  /**
   * @return the default bean factory.
   */
  public static BeanFactory getBeanFactory()
  {
    return beanFactory;
  }


  /**
   * Sets the default bean factory.<br>
   * <b>NOTE: this method changes the default bean factory for the whole
   * application! Use with care.</b>
   * 
   * @param beanFactory a not null bean factory.
   */
  public static void setBeanFactory(BeanFactory beanFactory)
  {
    Defaults.beanFactory = Assert.notNull(beanFactory);
  }


  /**
   * @return the default bean mapping strategy.
   */
  public static BeanMappingStrategy getBeanMappingStrategy()
  {
    return beanMappingStrategy;
  }


  /**
   * Sets the default bean mapping strategy.<br>
   * <b>NOTE: this method changes the default bean mapping strategy for the
   * whole application! Use with care.</b>
   * 
   * @param strategy a not null strategy.
   */
  public static void setBeanMappingStrategy(BeanMappingStrategy strategy)
  {
    Defaults.beanMappingStrategy = Assert.notNull(strategy);
  }


  /**
   * @return the default Java type handler registry.
   */
  public static JavaTypeHandlerRegistry getJavaTypeHandlerRegistry()
  {
    return javaTypeHandlerRegistry;
  }


  /**
   * Sets the default Java type handler registry.<br>
   * <b>NOTE: this method changes the default Java type handler registry for the
   * whole application! Use with care.</b>
   * 
   * @param registry a not null registry.
   */
  public static void setJavaTypeHandlerRegistry(JavaTypeHandlerRegistry registry)
  {
    Defaults.javaTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * See {@link JavaTypeHandlerRegistry#registerTypeHandler(JavaTypeHandler)} .
   */
  public static void registerJavaTypeHandler(JavaTypeHandler typeHandler)
  {
    getJavaTypeHandlerRegistry().registerTypeHandler(typeHandler);
  }


  /**
   * Sets the default value for 'strictFieldCheck'.<br>
   * <b>NOTE: this method changes the default value for the whole application!
   * Use with care.</b>
   * 
   * @param strictFieldCheck if true then all bean fields must be present in the
   *          result of the query.
   */
  public static void setStrictFieldCheck(boolean strictFieldCheck)
  {
    Defaults.strictFieldCheck = strictFieldCheck;
  }


  /**
   * @return true if all bean fields must be present in the result of the query.
   */
  public static boolean getStrictFieldCheck()
  {
    return strictFieldCheck;
  }

}
