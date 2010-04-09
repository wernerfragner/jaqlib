package org.jaqlib.util.db;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jaqlib.util.Assert;

/**
 * Caches one connection and prevents client code from closing this connection
 * by calling its <tt>close()</tt> method. In order to actually close this
 * connection the {@link #close()} method of this data source object must be
 * called. After calling this method the methods {@link #getConnection()} and
 * {@link #getConnection(String, String)} return new {@link Connection}
 * instances.
 * 
 * @author Werner Fragner
 */
public class SingleConnectionDataSource implements DataSource
{

  private Connection simpleConnection;
  private Connection usernamePasswordConnection;

  private final DataSource target;


  public SingleConnectionDataSource(DataSource target)
  {
    this.target = Assert.notNull(target);
  }


  public void close() throws SQLException
  {
    if (simpleConnection != null)
    {
      simpleConnection.close();
    }
    if (usernamePasswordConnection != null)
    {
      usernamePasswordConnection.close();
    }
  }


  public Connection getConnection() throws SQLException
  {
    if (simpleConnection == null)
    {
      simpleConnection = createConnectionWrapper(target.getConnection());
    }
    return simpleConnection;
  }


  public Connection getConnection(String username, String password)
      throws SQLException
  {
    if (usernamePasswordConnection == null)
    {
      usernamePasswordConnection = createConnectionWrapper(target
          .getConnection(username, password));
    }
    return usernamePasswordConnection;
  }


  public PrintWriter getLogWriter() throws SQLException
  {
    return target.getLogWriter();
  }


  public int getLoginTimeout() throws SQLException
  {
    return target.getLoginTimeout();
  }


  public void setLogWriter(PrintWriter out) throws SQLException
  {
    target.setLogWriter(out);
  }


  public void setLoginTimeout(int seconds) throws SQLException
  {
    target.setLoginTimeout(seconds);
  }


  public boolean isWrapperFor(Class<?> iface) throws SQLException
  {
    return target.isWrapperFor(iface);
  }


  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return target.unwrap(iface);
  }


  private Connection createConnectionWrapper(Connection connection)
  {
    return (Connection) Proxy.newProxyInstance(getClassLoader(),
        new Class[] { Connection.class }, new ConnectionInvocationHandler(
            connection));
  }


  private ClassLoader getClassLoader()
  {
    return Thread.currentThread().getContextClassLoader();
  }


  private static class ConnectionInvocationHandler implements InvocationHandler
  {

    private final Connection c;
    private boolean closed = false;


    public ConnectionInvocationHandler(Connection c)
    {
      this.c = c;
    }


    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
      if (method.getName().equals("equals"))
      {
        return c.equals(args[0]);
      }
      else if (method.getName().equals("hashCode"))
      {
        return c.hashCode();
      }
      else if (method.getName().equals("toString"))
      {
        return c.toString();
      }
      else if (method.getName().equals("close"))
      {
        closed = true;

        // do no close connection; connection is closed by datasource
        return null;
      }
      else if (method.getName().equals("isClosed"))
      {
        return closed;
      }
      else
      {
        try
        {
          return method.invoke(c, args);
        }
        catch (InvocationTargetException e)
        {
          throw e.getCause();
        }
      }
    }

  }


}
