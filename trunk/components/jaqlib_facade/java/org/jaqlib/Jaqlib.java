package org.jaqlib;

/**
 * Facade for the different query builders of JaQLib. <br/>
 * This class is the main entry point to JaQLib.
 * 
 * @author Werner Fragner
 */
public class Jaqlib
{

  /**
   * This class must not be instantiated.
   */
  private Jaqlib()
  {
    throw new UnsupportedOperationException(
        "This class must not be instantiated.");
  }


  /**
   * Gets a query builder for querying {@link Iterable} objects. For further
   * information see {@link IterableQueryBuilder}.
   * 
   * @return a query builder for querying {@link Iterable} objects.
   */
  public static IterableQueryBuilder List()
  {
    return IterableQB.getQueryBuilder();
  }


  /**
   * Gets a query builder for querying database tables. For further information
   * see {@link DatabaseQueryBuilder}.
   * 
   * @return a query builder for querying database tables.
   */
  public static DatabaseQueryBuilder Db()
  {
    return DatabaseQB.getQueryBuilder();
  }


}
