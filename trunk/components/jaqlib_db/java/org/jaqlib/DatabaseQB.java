package org.jaqlib;

import org.jaqlib.db.DbSelect;
import org.jaqlib.db.DbSelectResult;
import org.jaqlib.query.FromClause;
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.query.db.DatabaseQBProperties;
import org.jaqlib.query.db.DatabaseQueryBuilder;


/**
 * QB stands for QueryBuilder (see {@link DatabaseQueryBuilder} for further
 * information).
 * 
 * <p>
 * This class provides static helper methods to easily execute queries against
 * databases via JDBC. <br>
 * Examples are given here: {@link DatabaseQueryBuilder}.
 * </p>
 * 
 * @see DatabaseQueryBuilder
 * @author Werner Fragner
 */
public class DatabaseQB
{

  private static final DatabaseQBProperties PROPERTIES = new DatabaseQBProperties();
  private static final DatabaseQueryBuilder QUERYBUILDER = new DatabaseQueryBuilder(
      PROPERTIES);


  /**
   * Enables/disables strict checking if a column in the desired result does not
   * exist in the SELECT statement. If strict column check is enabled then an
   * exception is thrown if a column does exist in the SELECT statement. If
   * strict column check is disabled (DEFAULT) then an INFO log message is
   * issued and the column is ignored. If these INFO messages should not be
   * issued anymore then the JDK logger for
   * 'org.jaqlib.query.db.AbstractJaqLibOrMapper' must be disabled (see <a href=
   * "http//java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html">Java
   * Logging</a>).
   * 
   * @param strictColumnCheck
   */
  public void setStrictColumnCheck(boolean strictColumnCheck)
  {
    PROPERTIES.setStrictColumnCheck(strictColumnCheck);
  }


  /**
   * Selects a certain set of objects from a given database SELECT statement.
   * The SELECT statement that should be used must be specified in the returned
   * {@link FromClause}. The {@link FromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of user-defined {@link WhereCondition}s and user-defined
   * {@link ReflectiveWhereCondition}s.
   * 
   * @param <T> the result element type.
   * @param resultDefinition an object defining the desired result.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public static <T> FromClause<T, DbSelect> select(
      DbSelectResult<T> resultDefinition)
  {
    return QUERYBUILDER.select(resultDefinition);
  }


  /**
   * @param <T> the type of the result element(s).
   * @param resultElementClass a not null class of the result element(s).
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public static <T> T getMethodCallRecorder(Class<T> resultElementClass)
  {
    return QUERYBUILDER.getMethodCallRecorder(resultElementClass);
  }


}
