package org.jaqlib.query.db;

import java.util.Properties;

/**
 * Holds all properties of JaqLib for database query support.
 * 
 * @author Werner Fragner
 */
public class DatabaseQBProperties extends Properties
{

  public static final String STRICT_COLUMN_CHECK_KEY = "org.jaqlib.db.strictColumnCheck";
  public static final String STRICT_COLUMN_CHECK_DEFAULT = "false";


  /**
   * See {@link #setStrictColumnCheck(boolean)}.
   */
  public boolean getStrictColumnCheck()
  {
    return getBooleanProperty(STRICT_COLUMN_CHECK_KEY,
        STRICT_COLUMN_CHECK_DEFAULT);
  }


  /**
   * Enables/disables strict checking if a field in a Java bean does not exist
   * in the SELECT statement. If strict column check is enabled then an
   * exception is thrown if a Java bean field does exist in the SELECT
   * statement. If strict column check is disabled (DEFAULT) then an INFO log
   * message is issued and the field is ignored (= is not set). If these INFO
   * messages should not be issued then the JDK logger for
   * 'org.jaqlib.query.db.AbstractJaqLibOrMapper' must be disabled (see <a
   * href="
   * http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html">Java
   * Logging</a>).
   * 
   * @param strictColumnCheck enable/disable strict column check.
   */
  public void setStrictColumnCheck(boolean strictColumnCheck)
  {
    setProperty(STRICT_COLUMN_CHECK_KEY, Boolean.toString(strictColumnCheck));
  }


  private boolean getBooleanProperty(String key, String defaultValue)
  {
    final String value = getProperty(key, defaultValue);
    return Boolean.valueOf(value);
  }

}
