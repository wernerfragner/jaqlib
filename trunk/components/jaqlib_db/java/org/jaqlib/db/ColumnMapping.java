package org.jaqlib.db;

import java.sql.SQLException;
import java.sql.Types;


/**
 * Defines a primitive mapping between one database column (see columnName and
 * columnIndex) and a primitive Java type (see generic type T).
 * 
 * @author Werner Fragner
 * @param <T> The Java type of the mapping.
 */
public class ColumnMapping<T> extends AbstractMapping<T>
{

  private static final int NO_INDEX = Integer.MIN_VALUE;

  private String columnName;
  private int columnIndex = NO_INDEX;
  private int columnDataType = Types.OTHER;


  /**
   * Default constructor. When using this constructor eigther a column name or a
   * column index must be set.
   */
  public ColumnMapping()
  {
  }


  /**
   * @param columnName the database column name.
   */
  public ColumnMapping(String columnName)
  {
    setFieldName(columnName);
    setColumnName(columnName);
  }


  /**
   * @param columnIndex the database column index.
   */
  public ColumnMapping(int columnIndex)
  {
    setColumnIndex(columnIndex);
  }


  /**
   * @param columnName the database column name.
   */
  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
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
   * @return the database column name.
   */
  public String getColumnName()
  {
    return columnName;
  }


  /**
   * @return the database column index.
   */
  public int getColumnIndex()
  {
    return columnIndex;
  }


  private boolean hasColumnName()
  {
    return columnName != null;
  }


  private boolean hasColumnIndex()
  {
    // DB column index starts with 1
    return columnIndex > 0;
  }


  @Override
  @SuppressWarnings("unchecked")
  public T getValue(DbResultSet rs) throws SQLException
  {
    if (hasColumnIndex())
    {
      return (T) rs.getObject(columnDataType, columnIndex);
    }
    else if (hasColumnName())
    {
      return (T) rs.getObject(columnDataType, columnName);
    }
    else
    {
      throw handleInvalidMapping();
    }
  }


  @Override
  public String getLogString()
  {
    if (hasColumnName())
    {
      return getColumnName();
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


  private IllegalStateException handleInvalidMapping()
  {
    return new IllegalStateException(
        "Mapping must have a column index or a colum name.");
  }

}
