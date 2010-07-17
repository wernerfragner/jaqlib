package org.jaqlib.core.bean;

import org.jaqlib.Defaults;
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

  private Class<?> fieldType;
  private String targetName;
  private String sourceName;

  private JavaTypeHandler typeHandler = JavaTypeHandler.NULL;


  public FieldMapping()
  {
    super();
  }


  public FieldMapping(String fieldName)
  {
    setTargetName(fieldName);
    setSourceName(fieldName);
  }


  public FieldMapping(String fieldName, Class<?> fieldType)
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
  public Class<?> getFieldType()
  {
    return fieldType;
  }


  /**
   * @param fieldType the type of the Java bean field.
   */
  public void setFieldType(Class<?> fieldType)
  {
    this.fieldType = fieldType;
  }


  /**
   * @return the type handler for converting the source value type to the target
   *         value type. If none is set the default handler is used (see
   *         {@link Defaults}).
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
   * @param typeHandler
   */
  public void setTypeHandler(JavaTypeHandler typeHandler)
  {
    this.typeHandler = typeHandler;
  }


  @Override
  public String toString()
  {
    return getLogString();
  }


  @Override
  public String getLogString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(ReflectionUtil.getPlainClassName(fieldType));
    sb.append(": ");
    sb.append(getTargetName());
    sb.append(" <-> ");
    sb.append(getSourceName());
    return sb.toString();
  }


  @Override
  @SuppressWarnings("unchecked")
  public T getValue(DsResultSet rs)
  {
    return (T) rs.getObject(this);
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
