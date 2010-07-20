package org.jaqlib.db;

import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jaqlib.core.bean.BeanMapping;

/**
 * @author Werner Fragner
 */
public class DbDeleteDataSource extends AbstractDbDmlDataSource
{

  private String whereClause;


  public DbDeleteDataSource(DataSource dataSource, String tableName,
      String whereClause)
  {
    super(dataSource, tableName);
    setWhereClause(whereClause);
  }


  public String getWhereClause()
  {
    return whereClause;
  }


  public void setWhereClause(String whereClause)
  {
    this.whereClause = whereClause;
  }


  @Override
  protected <T> String buildSql(BeanMapping<T> beanMapping)
  {
    return buildSql();
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
