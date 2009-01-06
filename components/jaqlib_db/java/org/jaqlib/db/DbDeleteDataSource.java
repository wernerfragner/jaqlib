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

  private final String whereClause;
  private final String tableName;


  public DbDeleteDataSource(DataSource dataSource, String tableName,
      String whereClause)
  {
    super(dataSource);
    this.tableName = Assert.notNull(tableName);
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


  public void execute()
  {
    final String sql = buildSql();

    log.fine("Executing SQL DELETE statement: " + sql);

    try
    {
      final Statement stmt = getStatement();
      stmt.execute(sql);
      commit();
    }
    catch (SQLException e)
    {
      handleSqlException(e);
    }
    finally
    {
      close();
    }
  }

}
