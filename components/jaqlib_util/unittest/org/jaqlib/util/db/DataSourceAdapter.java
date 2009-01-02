package org.jaqlib.util.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Adapter implementation of the {@link javax.sql.DataSource} interface. All
 * implemented methods throw {@link UnsupportedOperationException}. Extending
 * classes are meant to override all desired methods.
 * 
 * @author Werner Fragner
 */
public class DataSourceAdapter implements DataSource
{

  public Connection getConnection() throws SQLException
  {
    throw new UnsupportedOperationException();
  }


  public Connection getConnection(String username, String password)
      throws SQLException
  {
    throw new UnsupportedOperationException();
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
