package org.jaqlib.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jaqlib.util.ReflectionUtil;

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


  /**
   * @return a printable log string describing this mapping.
   */
  public abstract String getLogString();


  public abstract void appendColumn(StringBuilder columns, StringBuilder values);


  protected Object getFieldValue(Object bean)
  {
    return ReflectionUtil.getFieldValue(bean, getFieldName());
  }


  public <BeanType> void setValue(int index, PreparedStatement stmt,
      BeanType bean, BeanMapping<BeanType> beanMapping) throws SQLException
  {
    Object value = getFieldValue(bean);
    value = beanMapping.applyJavaTypeHandler(getFieldName(), value);
    stmt.setObject(index, value);
  }

}
