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
    return resultSet;
  }


  public void close()
  {
    DbUtil.close(resultSet);
    resultSet = null;
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
