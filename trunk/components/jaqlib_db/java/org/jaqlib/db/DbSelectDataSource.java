package org.jaqlib.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;
import org.jaqlib.util.DbUtil;
import org.jaqlib.util.LogUtil;

/**
 * <p>
 * Represents a SELECT statement on a given JDBC connection. The result mapping
 * can be customized using following methods:
 * <ul>
 * <li>{@link #setStrictColumnCheck(boolean)}</li>
 * <li>{@link #setSqlTypeHandlerRegistry(SqlTypeHandlerRegistry)}</li>
 * <li>{@link #registerSqlTypeHandler(int, SqlTypeHandler)}</li>
 * </ul>
 * See according method javadoc for further details).
 * </p>
 * 
 * @author Werner Fragner
 */
public class DbSelectDataSource
{

  private final Logger log = LogUtil.getLogger(this);

  private final String sql;
  private final DataSource dataSource;

  private boolean strictColumnCheck = Defaults.getStrictColumnCheck();
  private SqlTypeHandlerRegistry sqlTypeHandlerRegistry = Defaults
      .getSqlTypeHandlerRegistry();

  private Connection connection;
  private Statement statement;
  private DbResultSet resultSet;


  public DbSelectDataSource(DataSource dataSource, String sql)
  {
    this.dataSource = Assert.notNull(dataSource);
    this.sql = Assert.notNull(sql);
  }


  /**
   * Registers the given custom SQL type handler with the given SQL data type.
   * 
   * @param sqlDataType a SQL data type as defined at {@link java.sql.Types}.
   * @param typeHandler a not null type handler.
   */
  public void registerSqlTypeHandler(int sqlDataType, SqlTypeHandler typeHandler)
  {
    sqlTypeHandlerRegistry.registerTypeHandler(sqlDataType, typeHandler);
  }


  /**
   * Changes the SQL type handler registry to a custom implementation. By
   * default the standard SQL types are supported.
   * 
   * @param registry a custom SQL type handler registry.
   */
  public void setSqlTypeHandlerRegistry(SqlTypeHandlerRegistry registry)
  {
    this.sqlTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * When mapping the result of a SQL SELECT statement to the fields of a Java
   * bean then an INFO log message is issued when a Java bean field does not
   * exist in the SQL SELECT statement result. This behavior can be changed by
   * setting the property <b>strictColumnCheck</b> to true. In that case a
   * {@link DataSourceQueryException} is thrown instead of issuing the INFO log
   * message. If no INFO log message should be issued then the JDK logger for
   * <tt>org.jaqlib.db.DbResultSet</tt> must be disabled. How to do that is
   * described at <a href=
   * "http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html">Java
   * Logging</a>.
   * 
   * @param strictColumnCheck enable/disable strict column check.
   */
  public void setStrictColumnCheck(boolean strictColumnCheck)
  {
    this.strictColumnCheck = strictColumnCheck;
  }


  public String getSql()
  {
    return sql;
  }


  public DbResultSet execute() throws SQLException
  {
    log.fine("Executing SQL statement: " + sql);

    final ResultSet rs = getStatement().executeQuery(sql);
    resultSet = new DbResultSet(rs, sqlTypeHandlerRegistry, strictColumnCheck);
    return resultSet;
  }


  public void close()
  {
    close(resultSet);
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
      log.fine("Creating SQL statement");

      statement = getConnection().createStatement();
    }
    return statement;
  }


  private Connection getConnection() throws SQLException
  {
    if (connection == null)
    {
      log.fine("Getting JDBC connection");

      connection = dataSource.getConnection();
    }
    return connection;
  }


  private void close(DbResultSet rs)
  {
    if (rs != null)
    {
      rs.close();
    }
  }


  @Override
  public String toString()
  {
    return "[SQL: " + sql + "]";
  }


}
