package org.jaqlib;

/**
 * <p>
 * Facade for the different query builders of JaQLib. <br/>
 * This class is the main entry point to JaQLib.
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
   * This class must not be instantiated.
   */
  private Jaqlib()
  {
    throw new UnsupportedOperationException(
        "This class must not be instantiated.");
  }

}
