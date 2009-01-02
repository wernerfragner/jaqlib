package org.jaqlib.util;

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
   * Completely disable logging of JaQLib.
   */
  public static void disableJaqLibLogging()
  {
    setJaqLibLogLevel(Level.OFF);
  }


  /**
   * Sets the log level of the JaQLib JDK logger.
   * 
   * @param level
   */
  public static void setJaqLibLogLevel(Level level)
  {
    getJaqLibLogger().setLevel(level);
  }


  /**
   * @return the JDK logger for JaQLib. This logger can be used to add
   *         additional {@link java.util.logging.Handler}s or to customize the
   *         logging process.
   */
  public static Logger getJaqLibLogger()
  {
    return Logger.getLogger("org.jaqlib");
  }

}
