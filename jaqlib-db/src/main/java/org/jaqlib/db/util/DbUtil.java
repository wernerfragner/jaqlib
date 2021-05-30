package org.jaqlib.db.util;

import org.jaqlib.util.LogUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Helper class for accessing databases via JDBC.
 * 
 * @author Werner Fragner
 */
public class DbUtil
{

  public static void executeStatement(Connection c, String sql)
      throws SQLException
  {
    Statement s = null;

    try
    {
      s = c.createStatement();
      s.execute(sql);
    }
    finally
    {
      close(s);
    }
  }


  public static void close(Connection c)
  {
    if (c != null)
    {
      try
      {
        c.close();
      }
      catch (SQLException e)
      {
        getLog().info("Could not close JDBC connection");
      }
    }
  }


  public static void close(Statement s)
  {
    if (s != null)
    {
      try
      {
        s.close();
      }
      catch (SQLException e)
      {
        getLog().info("Could not close JDBC statement");
      }
    }
  }


  public static void close(ResultSet rs)
  {
    if (rs != null)
    {
      try
      {
        rs.close();
      }
      catch (SQLException e)
      {
        getLog().info("Could not close JDBC result set");
      }
    }
  }


  private static Logger getLog()
  {
    return LogUtil.getLogger(DbUtil.class);
  }

}
