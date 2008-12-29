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


  public boolean getStrictColumnCheck()
  {
    return getBooleanProperty(STRICT_COLUMN_CHECK_KEY,
        STRICT_COLUMN_CHECK_DEFAULT);
  }


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
