package org.jaqlib.util.db;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Adapter implementation of the {@link javax.sql.DataSource} interface. All
 * implemented methods throw {@link UnsupportedOperationException}. Extending
 * classes are meant to override all desired methods.
 * 
 * @author Werner Fragner
 */
public class DataSourceAdapter implements DataSource
{

  @Override
  public Connection getConnection() throws SQLException
  {
    throw new UnsupportedOperationException();
  }


  @Override
  public Connection getConnection(String username, String password)
      throws SQLException
  {
    throw new UnsupportedOperationException();
  }


  @Override
  public PrintWriter getLogWriter()
  {
    throw new UnsupportedOperationException();
  }


  @Override
  public int getLoginTimeout()
  {
    throw new UnsupportedOperationException();
  }


  @Override
  public void setLogWriter(PrintWriter out)
  {
    throw new UnsupportedOperationException();
  }


  @Override
  public void setLoginTimeout(int seconds)
  {
    throw new UnsupportedOperationException();
  }


  @Override
  public boolean isWrapperFor(Class<?> iface)
  {
    throw new UnsupportedOperationException();
  }


  @Override
  public <T> T unwrap(Class<T> iface)
  {
    throw new UnsupportedOperationException();
  }


  @Override
  public Logger getParentLogger()
  {
    throw new UnsupportedOperationException();
  }

}
