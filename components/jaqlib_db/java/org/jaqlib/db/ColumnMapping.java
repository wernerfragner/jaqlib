package org.jaqlib.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.FieldMapping;


/**
 * Defines a primitive mapping between one database column (see columnName and
 * columnIndex) and a primitive Java type (see generic type T).
 * 
 * @author Werner Fragner
 * @param <T> The Java type of the mapping.
 */
public class ColumnMapping<T> extends FieldMapping<T>
{

  private static final int NO_INDEX = Integer.MIN_VALUE;

  private String columnLabel;
  private String columnName;
  private int columnIndex = NO_INDEX;
  private int columnDataType = Types.OTHER;


  /**
   * Default constructor. When using this constructor either a column name or a
   * column index must be set.
   */
  public ColumnMapping()
  {
  }


  /**
   * @param columnLabel the database column name.
   */
  public ColumnMapping(String columnLabel)
  {
    super(columnLabel);
    setColumnLabel(columnLabel);
  }


  /**
   * @param columnIndex the database column index.
   */
  public ColumnMapping(int columnIndex)
  {
    setColumnIndex(columnIndex);
  }


  /**
   * @param columnLabel the database column label.
   */
  public void setColumnLabel(String columnLabel)
  {
    this.columnLabel = columnLabel;
  }


  /**
   * @param columnIndex the database column index.
   */
  public void setColumnIndex(int columnIndex)
  {
    this.columnIndex = columnIndex;
  }


  /**
   * Optional method. If no column data type is specified then the database
   * driver performs data type conversion from DB types to Java types.
   * 
   * @param columnDataType the data type of this column at database. The type
   *          must be one as defined at {@link java.sql.Types}.
   */
  public void setColumnDataType(int columnDataType)
  {
    this.columnDataType = columnDataType;
  }


  /**
   * @return the data type of this column at database. The type must be one as
   *         defined at {@link java.sql.Types}.
   */
  public int getColumnDataType()
  {
    return columnDataType;
  }


  /**
   * @return the database column label.
   */
  public String getColumnLabel()
  {
    return columnLabel;
  }


  /**
   * @return the database column index.
   */
  public int getColumnIndex()
  {
    return columnIndex;
  }


  /**
   * @return the database column name.
   */
  public String getColumnName()
  {
    return columnName;
  }


  /**
   * @param columnName the database column name.
   */
  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }


  boolean hasColumnLabel()
  {
    return columnLabel != null;
  }


  private boolean hasColumnName()
  {
    return columnName != null;
  }


  boolean hasColumnIndex()
  {
    // DB column index starts with 1
    return columnIndex > 0;
  }


  public <BeanType> void setValue(int index, PreparedStatement stmt,
      BeanType bean, BeanMapping<BeanType> beanMapping) throws SQLException
  {
    Object value = getFieldValue(bean);
    value = beanMapping.applyJavaTypeHandler(getFieldName(), value);
    if (value == null)
    {
      stmt.setNull(index, getColumnDataType(bean));
    }
    else
    {
      stmt.setObject(index, value);
    }
  }


  private int getColumnDataType(Object bean)
  {
    if (columnDataType == Types.OTHER)
    {
      return getBestMatchColumnDataType(bean);
    }
    else
    {
      return columnDataType;
    }
  }


  private int getBestMatchColumnDataType(Object bean)
  {
    Class<?> fieldType = getFieldType(bean);
    if (fieldType.equals(String.class))
    {
      return Types.VARCHAR;
    }
    else if (fieldType.equals(Number.class))
    {
      return Types.NUMERIC;
    }
    else if (fieldType.equals(java.util.Date.class))
    {
      return Types.TIMESTAMP;
    }
    else if (fieldType.equals(Timestamp.class))
    {
      return Types.TIMESTAMP;
    }
    else
    {
      return Types.OTHER;
    }
  }


  @Override
  public String getLogString()
  {
    if (hasColumnLabel())
    {
      return getColumnLabel();
    }
    else if (hasColumnIndex())
    {
      return String.valueOf(getColumnIndex());
    }
    else
    {
      throw handleInvalidMapping();
    }
  }


  public void appendColumn(StringBuilder columns, StringBuilder values)
  {
    columns.append(getColumnForSql());
    values.append("?");
  }


  public void appendColumn(StringBuilder updateSql)
  {
    updateSql.append(getColumnForSql());
    updateSql.append(" = ?");
  }


  private String getColumnForSql()
  {
    String column = null;
    if (hasColumnName())
    {
      column = getColumnName();
    }
    else if (hasColumnLabel())
    {
      column = getColumnLabel();
    }
    else
    {
      handleInvalidMapping();
    }
    return column;
  }


  private IllegalStateException handleInvalidMapping()
  {
    return new IllegalStateException(
        "Mapping must have a column index or a colum name.");
  }


  public static <T> ColumnMapping<T> cast(FieldMapping<T> mapping)
  {
    if (mapping instanceof ColumnMapping<?>)
    {
      return (ColumnMapping<T>) mapping;
    }

    return new ColumnMapping<T>(mapping.getFieldName());
  }

}
