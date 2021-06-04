package org.jaqlib.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Werner Fragner
 */
public class DriverManagerDataSource extends DataSourceAdapter
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
    if (driverClassName == null)
    {
      return;
    }

    try
    {
      Class.forName(driverClassName);
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }

  }


  @Override
  public Connection getConnection() throws SQLException
  {
    registerDriver();
    return DriverManager.getConnection(url);
  }


  @Override
  public Connection getConnection(String username, String password)
      throws SQLException
  {
    registerDriver();
    return DriverManager.getConnection(url, username, password);
  }

}
