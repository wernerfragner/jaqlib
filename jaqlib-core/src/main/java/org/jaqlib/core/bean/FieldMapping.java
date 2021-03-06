package org.jaqlib.core.bean;

import org.jaqlib.core.Defaults;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.util.ReflectionUtil;

/**
 * Represents the mapping between a source field (e.g. database column, XML
 * element, ...) to a Java been field.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the type of the Java bean field.
 */
public class FieldMapping<T> extends AbstractMapping<T>
{

  private Class<? extends T> fieldType;
  private String targetName;
  private String sourceName;

  private JavaTypeHandler typeHandler = JavaTypeHandler.NULL;


  public FieldMapping()
  {
    super();
  }


  /**
   * Constructs a new field mapping by using the given name as source and target
   * name.
   * 
   * @param fieldName the Java bean field name.
   */
  public FieldMapping(String fieldName)
  {
    setTargetName(fieldName);
    setSourceName(fieldName);
  }


  /**
   * Constructs a new field mapping by using the given name as source and target
   * name.
   * 
   * @param fieldName the Java bean field name.
   * @param fieldType the type of the Java bean field.
   */
  public FieldMapping(String fieldName, Class<T> fieldType)
  {
    this(fieldName);
    setFieldType(fieldType);
  }


  /**
   * @param fieldName the Java bean field name.
   */
  public void setTargetName(String fieldName)
  {
    this.targetName = fieldName;
  }


  /**
   * @return the Java bean field name.
   */
  public String getTargetName()
  {
    return targetName;
  }


  /**
   * @return the name of the field at the source.
   */
  public String getSourceName()
  {
    return sourceName;
  }


  /**
   * @param targetName the name of the field at the source.
   */
  public void setSourceName(String targetName)
  {
    this.sourceName = targetName;
  }


  /**
   * @return the type of the Java bean field.
   */
  public Class<? extends T> getFieldType()
  {
    return fieldType;
  }


  /**
   * @param fieldType the type of the Java bean field.
   */
  public void setFieldType(Class<? extends T> fieldType)
  {
    this.fieldType = fieldType;
  }


  /**
   * @return the type handler for converting the source value type to the target
   *         value type. If none is set the default handler is used (see
   *         {@link Defaults#getJavaTypeHandlerRegistry()}).
   */
  public JavaTypeHandler getTypeHandler()
  {
    if (typeHandler == JavaTypeHandler.NULL)
    {
      return Defaults.getJavaTypeHandlerRegistry().getTypeHandler(
          getFieldType());
    }
    else
    {
      return typeHandler;
    }
  }


  /**
   * Sets a custom type handler for converting the source value type to the
   * target value type.
   * 
   * @param typeHandler hte type handler to set.
   */
  public void setTypeHandler(JavaTypeHandler typeHandler)
  {
    this.typeHandler = typeHandler;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return getLogString();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getLogString()
  {
    return ReflectionUtil.getPlainClassName(fieldType) +
            ": " +
            getTargetName() +
            " <-> " +
            getSourceName();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public T getValue(DsResultSet rs)
  {
    return (T) rs.getObject(this);
  }


  /**
   * Extracts the anonymous value that is defined by this class (type and type
   * handler) from the given result set.
   * 
   * @param rs the data source result set.
   * @return the value from the result set.
   */
  @SuppressWarnings("unchecked")
  public T getAnonymousValue(DsResultSet rs)
  {
    return (T) rs.getAnynomousObject(this);
  }


  protected Object getFieldValue(Object bean)
  {
    return ReflectionUtil.getFieldValue(bean, getTargetName());
  }


  protected Class<?> getFieldType(Object bean)
  {
    return ReflectionUtil.getFieldType(bean.getClass(), getTargetName());
  }

}
