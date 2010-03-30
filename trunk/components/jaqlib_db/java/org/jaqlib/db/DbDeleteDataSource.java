package org.jaqlib.db;

import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public class DbDeleteDataSource extends AbstractDbDataSource
{

  private String whereClause;
  private String tableName;


  public DbDeleteDataSource(DataSource dataSource, String tableName,
      String whereClause)
  {
    super(dataSource);
    setTableName(tableName);
    setWhereClause(whereClause);
  }


  public void setTableName(String tableName)
  {
    this.tableName = Assert.notNull(tableName);
  }


  public String getTableName()
  {
    return tableName;
  }


  public String getWhereClause()
  {
    return whereClause;
  }


  public void setWhereClause(String whereClause)
  {
    this.whereClause = whereClause;
  }


  private String buildSql()
  {
    StringBuilder deleteSql = new StringBuilder();
    deleteSql.append("DELETE FROM ");
    deleteSql.append(tableName);
    appendWhereClause(deleteSql, whereClause);
    return deleteSql.toString();
  }


  public int execute()
  {
    final String sql = buildSql();

    log.fine("Executing SQL DELETE statement: " + sql);

    try
    {
      final Statement stmt = getStatement();
      stmt.execute(sql);
      commit();
      return stmt.getUpdateCount();
    }
    catch (SQLException e)
    {
      throw handleSqlException(e);
    }
    finally
    {
      closeAfterQuery();
    }
  }

}
