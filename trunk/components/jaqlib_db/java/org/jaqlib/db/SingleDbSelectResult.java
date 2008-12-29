package org.jaqlib.db;

/**
 * @author Werner Fragner
 */
public class SingleDbSelectResult<T> extends DbSelectResult<T>
{

  private static final int NO_INDEX = Integer.MIN_VALUE;

  private String columnName;
  private int columnIndex = NO_INDEX;


  public SingleDbSelectResult(String columnName)
  {
    setFieldName(columnName);
    setColumnName(columnName);
  }


  public SingleDbSelectResult(int columnIndex)
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
