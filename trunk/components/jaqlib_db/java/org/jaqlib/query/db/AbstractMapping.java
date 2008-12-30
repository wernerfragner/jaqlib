package org.jaqlib.query.db;

import java.sql.SQLException;

import org.jaqlib.util.db.DbResultSet;


/**
 * @author Werner Fragner
 */
public abstract class AbstractMapping<T>
{

  private String fieldName;


  /**
   * @return the Java bean field name.
   */
  public String getFieldName()
  {
    return fieldName;
  }


  /**
   * 
   * @param fieldName the Java bean field name.
   */
  public void setFieldName(String fieldName)
  {
    this.fieldName = fieldName;
  }


  /**
   * Extracts the value that is defined by this class from the given result set.
   * 
   * @param rs the database result set.
   * @return the value from the result set.
   */
  public abstract T getValue(DbResultSet rs) throws SQLException;

}
