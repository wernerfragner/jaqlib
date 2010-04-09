package org.jaqlib.core.bean;

import org.jaqlib.core.DsResultSet;
import org.jaqlib.util.ReflectionUtil;

/**
 * @author Werner Fragner
 */
public abstract class AbstractMapping<T>
{

  private String fieldName;


  public AbstractMapping()
  {
    super();
  }


  public AbstractMapping(String fieldName)
  {
    super();
    setFieldName(fieldName);
  }


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
  public abstract T getValue(DsResultSet rs);


  /**
   * @return a printable log string describing this mapping.
   */
  public abstract String getLogString();


  protected Object getFieldValue(Object bean)
  {
    return ReflectionUtil.getFieldValue(bean, getFieldName());
  }


  protected Class<?> getFieldType(Object bean)
  {
    return ReflectionUtil.getFieldType(bean.getClass(), getFieldName());
  }


}
