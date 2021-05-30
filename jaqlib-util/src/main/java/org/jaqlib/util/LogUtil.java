package org.jaqlib.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for controlling logging of JaQLib. JaQLib uses the standard JDK
 * logging mechanism (see {@link java.util.logging.Logger}).
 * 
 * @see java.util.logging.Logger
 * @author Werner Fragner
 */
public class LogUtil
{

  private static final ConsoleHandler handler = new ConsoleHandler();

  static
  {
    handler.setLevel(Level.ALL);
  }


  /**
   * Gets the JDK logger for JaqLib.
   * 
   * @return see description.
   */
  public static Logger getJaqlibLogger()
  {
    return Logger.getLogger("org.jaqlib");
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
    registerLogHandler(handler, Level.ALL);
  }


  /**
   * <p>
   * Disables logging of all JaQLib messages to the console.
   * </p>
   * By default logging to the console is disabled.
   */
  public static void disableConsoleLogging()
  {
    unregisterLogHandler(handler);
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
   * @param cls a not null class.
   * @return the logger for the given class.
   */
  public static Logger getLogger(Class<?> cls)
  {
    return Logger.getLogger(cls.getName());
  }


  /**
   * @param obj a not null object.
   * @return the logger for the given object.
   */
  public static Logger getLogger(Object obj)
  {
    if (obj instanceof Class<?>)
    {
      return getLogger((Class<?>) obj);
    }
    else
    {
      return getLogger(obj.getClass());
    }
  }

}
