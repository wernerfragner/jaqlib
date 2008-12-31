package org.jaqlib.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;
import org.jaqlib.util.db.DbUtil;

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
   * @param registry a user-defined SQL type handler registry.
   */
  public void setSqlTypeHandlerRegistry(SqlTypeHandlerRegistry registry)
  {
    this.sqlTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * Enables/disables strict checking if a field in a Java bean does not exist
   * in the SELECT statement. If strict column check is enabled then an
   * exception is thrown if a Java bean field does exist in the SELECT
   * statement. If strict column check is disabled (DEFAULT) then an INFO log
   * message is issued and the field is ignored (= is not set). If these INFO
   * messages should not be issued then the JDK logger for
   * 'org.jaqlib.query.db.AbstractJaqLibOrMapper' must be disabled (see <a
   * href="
   * http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html">Java
   * Logging</a>).
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


  private void close(DbResultSet rs)
  {
    if (rs != null)
    {
      rs.close();
    }
  }


}
