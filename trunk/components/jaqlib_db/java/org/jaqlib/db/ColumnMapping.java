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

  private String columnLabel;
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
   * @param columnLabel the database column name.
   */
  public ColumnMapping(String columnLabel)
  {
    setFieldName(columnLabel);
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
   * @param the database column name.
   */
  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }


  private boolean hasColumnLabel()
  {
    return columnLabel != null;
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
    else if (hasColumnLabel())
    {
      return (T) rs.getObject(columnDataType, columnLabel);
    }
    else
    {
      throw handleInvalidMapping();
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


  private IllegalStateException handleInvalidMapping()
  {
    return new IllegalStateException(
        "Mapping must have a column index or a colum name.");
  }


  @Override
  public void appendColumn(StringBuilder columns, StringBuilder values)
  {
    columns.append(getColumnForSql());
    values.append("?");
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


  @Override
  public void appendColumn(StringBuilder updateSql)
  {
    updateSql.append(getColumnForSql());
    updateSql.append(" = ?");
  }

}
