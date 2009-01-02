package org.jaqlib.util;

import java.util.logging.ConsoleHandler;
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

  /**
   * Enables logging of all JaQLib messages to the console.
   */
  public static void enableConsoleLogging()
  {
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(Level.ALL);

    Logger logger = Logger.getLogger("org.jaqlib");
    logger.setLevel(Level.ALL);
    logger.addHandler(handler);
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
    if (obj instanceof Class)
    {
      return getLogger((Class<?>) obj);
    }
    else
    {
      return getLogger(obj.getClass());
    }
  }

}
