package org.jaqlib;

import org.jaqlib.core.DefaultsDelegate;


/**
 * <p>
 * Facade for the different query builders of JaQLib. <br/>
 * This class is the main entry point of JaQLib.
 * </p>
 * Usage examples are given here:
 * <ul>
 * <li>{@link IterableQueryBuilder}</li>
 * <li>{@link DatabaseQueryBuilder}</li>
 * <li>{@link XmlQueryBuilder}</li>
 * </ul>
 * This class is thread-safe.
 * 
 * @author Werner Fragner
 */
public class Jaqlib
{

  /**
   * Static instance for querying {@link Iterable} objects. For further
   * information see {@link IterableQueryBuilder}.
   */
  public static IterableQueryBuilder List = IterableQB.getQueryBuilder();

  /**
   * Static instance for querying database tables. For further information see
   * {@link DatabaseQueryBuilder}.
   */
  public static DatabaseQueryBuilder DB = DatabaseQB.getQueryBuilder();

  /**
   * Static instance for querying XML files. For further information see
   * {@link XmlQueryBuilder}.
   */
  public static XmlQueryBuilder XML = XmlQB.getQueryBuilder();


  /**
   * Contains all general application-wide JaqLib default values. The domain
   * specific default values (Iterable, DB, XML) can be found in the according
   * static fields (e.g. <tt>Jaqlib.DB.DEFAULTS</tt>).
   */
  public static DefaultsDelegate DEFAULTS = new DefaultsDelegate();


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
   * you can use <tt>Jaqlib.DEFAULTS.registerLogHandler(Handler, Level)</tt>.
   * </p>
   */
  public static void enableConsoleLogging()
  {
    DEFAULTS.enableConsoleLogging();
  }


  /**
   * <p>
   * Disables logging of all JaQLib messages to the console.
   * </p>
   * By default logging to the console is disabled.
   */
  public static void disableConsoleLogging()
  {
    DEFAULTS.disableConsoleLogging();
  }


  /**
   * This class must not be instantiated.
   */
  private Jaqlib()
  {
    throw new UnsupportedOperationException(
        "This class must not be instantiated.");
  }

}
