package org.jaqlib.core.bean;

import org.jaqlib.core.Defaults;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.util.ReflectionUtil;

public class FieldMapping<T> extends AbstractMapping<T>
{

  private Class<?> fieldType;
  private String targetName;

  private JavaTypeHandler typeHandler = JavaTypeHandler.NULL;


  public FieldMapping()
  {
    super();
  }


  public FieldMapping(String fieldName)
  {
    super(fieldName);
    setTargetName(fieldName);
  }


  public FieldMapping(String fieldName, Class<?> fieldType)
  {
    this(fieldName);
    setFieldType(fieldType);
  }


  public String getTargetName()
  {
    return targetName;
  }


  public void setTargetName(String targetName)
  {
    this.targetName = targetName;
  }


  public Class<?> getFieldType()
  {
    return fieldType;
  }


  public void setFieldType(Class<?> fieldType)
  {
    this.fieldType = fieldType;
  }


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
    sb.append(getFieldName());
    sb.append(" <-> ");
    sb.append(getTargetName());
    return sb.toString();
  }


  @Override
  @SuppressWarnings("unchecked")
  public T getValue(DsResultSet rs)
  {
    return (T) rs.getObject(this);
  }

}
