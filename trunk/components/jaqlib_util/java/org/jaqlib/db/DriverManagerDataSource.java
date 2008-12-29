package org.jaqlib.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @author Werner Fragner
 */
public class DriverManagerDataSource implements DataSource
{

  private String driverClassName;
  private String url;


  public void setDriverClassName(String driverClassName)
  {
    this.driverClassName = driverClassName;
  }


  public void setUrl(String url)
  {
    this.url = url;
  }


  private void registerDriver()
  {
    try
    {
      Class.forName(driverClassName);
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }

  }


  public Connection getConnection() throws SQLException
  {
    registerDriver();
    return DriverManager.getConnection(url);
  }


  public Connection getConnection(String username, String password)
      throws SQLException
  {
    registerDriver();
    return DriverManager.getConnection(url, username, password);
  }


  public PrintWriter getLogWriter() throws SQLException
  {
    throw new UnsupportedOperationException();
  }


  public int getLoginTimeout() throws SQLException
  {
    throw new UnsupportedOperationException();
  }


  public void setLogWriter(PrintWriter out) throws SQLException
  {
    throw new UnsupportedOperationException();
  }


  public void setLoginTimeout(int seconds) throws SQLException
  {
    throw new UnsupportedOperationException();
  }


  public boolean isWrapperFor(Class<?> iface) throws SQLException
  {
    throw new UnsupportedOperationException();
  }


  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    throw new UnsupportedOperationException();
  }


}
