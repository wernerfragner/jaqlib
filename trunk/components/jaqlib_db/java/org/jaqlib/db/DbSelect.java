package org.jaqlib.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public class DbSelect
{

  private final String sql;
  private final DataSource dataSource;

  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;

  private DbResultSetMetaData resultSetMetaData;


  public DbSelect(DataSource dataSource, String sql)
  {
    this.dataSource = Assert.notNull(dataSource);
    this.sql = Assert.notNull(sql);
  }


  public String getSql()
  {
    return sql;
  }


  public ResultSet execute() throws SQLException
  {
    resultSet = getStatement().executeQuery(sql);
    resultSetMetaData = new DbResultSetMetaData(resultSet.getMetaData());
    return resultSet;
  }


  public boolean hasColumn(String columnName) throws SQLException
  {
    Assert.notNull(resultSetMetaData,
        "The SELECT statement must be executed before calling this method.");
    return resultSetMetaData.hasColumn(columnName);
  }


  public void close()
  {
    DbUtil.close(resultSet);
    resultSet = null;
    resultSetMetaData = null;
    DbUtil.close(statement);
    statement = null;
    DbUtil.close(connection);
    connection = null;
  }


  private Statement getStatement() throws SQLException
  {
    if (statement == null)
    {
      statement = getConnection().createStatement();
    }
    return statement;
  }


  private Connection getConnection() throws SQLException
  {
    if (connection == null)
    {
      connection = dataSource.getConnection();
    }
    return connection;
  }

}
