package org.jaqlib.db;

/**
 * Represents a database table column. The column can be identified using the
 * column name (the actual name or the alias defined in the SELECT statements)
 * or the column index of the SELECT statement (starting with 1 - in conformity
 * with JDBC conventions).
 * 
 * @author Werner Fragner
 * 
 * @param <T> the java type of the database table column.
 */
public class Column<T>
{

  private static final int NO_INDEX = Integer.MIN_VALUE;

  private String columnName;
  private int columnIndex = NO_INDEX;


  public Column(String columnName)
  {
    setColumnName(columnName);
  }


  public Column(int columnIndex)
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
