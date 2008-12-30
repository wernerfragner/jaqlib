package org.jaqlib.query.db;

/**
 * Defines a primitive mapping between one database column (see columnName and
 * columnIndex) and a primitive Java type (see generic type T).
 * 
 * @author Werner Fragner
 * @param <T> The Java type of the mapping.
 */
public class PrimitiveDbSelectResult<T> extends DbSelectResult<T>
{

  private static final int NO_INDEX = Integer.MIN_VALUE;

  private String columnName;
  private int columnIndex = NO_INDEX;


  public PrimitiveDbSelectResult()
  {
  }


  public PrimitiveDbSelectResult(String columnName)
  {
    setFieldName(columnName);
    setColumnName(columnName);
  }


  public PrimitiveDbSelectResult(int columnIndex)
  {
    setColumnIndex(columnIndex);
  }


  public void setColumnName(String name)
  {
    this.columnName = name;
  }


  public void setColumnIndex(int index)
  {
    this.columnIndex = index;
  }


  public String getColumnName()
  {
    return columnName;
  }


  public int getColumnIndex()
  {
    return columnIndex;
  }


  public boolean hasColumnName()
  {
    return columnName != null;
  }


  public boolean hasColumnIndex()
  {
    // DB column index starts with 1
    return columnIndex > 0;
  }


}
