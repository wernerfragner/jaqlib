package org.jaqlib.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.jaqlib.util.CollectionUtil;

/**
 * @author Werner Fragner
 */
public class DbResultSetMetaData
{

  private final ResultSetMetaData metaData;

  private Map<String, DbColumnMetaData> columns;


  public DbResultSetMetaData(ResultSetMetaData metaData)
  {
    this.metaData = metaData;
  }


  public boolean hasColumn(String columnName) throws SQLException
  {
    columnName = columnName.toUpperCase();
    return (getColumns().containsKey(columnName));
  }


  private Map<String, DbColumnMetaData> getColumns() throws SQLException
  {
    if (columns == null)
    {
      columns = createColumns();
    }
    return columns;
  }


  private Map<String, DbColumnMetaData> createColumns() throws SQLException
  {
    Map<String, DbColumnMetaData> columns = CollectionUtil.newDefaultMap();

    for (int i = 1; i < metaData.getColumnCount() + 1; i++)
    {
      DbColumnMetaData columnMetaData = createColumnMetaData(metaData, i);
      columns.put(columnMetaData.getName(), columnMetaData);
    }

    return columns;
  }


  private DbColumnMetaData createColumnMetaData(ResultSetMetaData metaData,
      int columnIndex) throws SQLException
  {
    DbColumnMetaData columnMetaData = new DbColumnMetaData();
    columnMetaData.setName(metaData.getColumnLabel(columnIndex));
    columnMetaData.setIndex(columnIndex);
    return columnMetaData;
  }

}
