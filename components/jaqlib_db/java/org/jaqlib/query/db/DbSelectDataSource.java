package org.jaqlib.query.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jaqlib.util.Assert;
import org.jaqlib.util.db.DbResultSet;
import org.jaqlib.util.db.DbUtil;
import org.jaqlib.util.db.DefaultTypeHandlerRegistry;
import org.jaqlib.util.db.TypeHandler;
import org.jaqlib.util.db.TypeHandlerRegistry;

/**
 * Represents a SELECT statement on a given JDBC connection. The result mapping
 * can be customized using the methods {@link #setStrictColumnCheck(boolean)},
 * {@link #setTypeHandlerRegistry(TypeHandlerRegistry)} and
 * {@link #getTypeHandlerRegistry()} (see according method javadoc for further
 * details).
 * 
 * @author Werner Fragner
 */
public class DbSelectDataSource
{

  private final String sql;
  private final DataSource dataSource;
  private TypeHandlerRegistry typeHandlerRegistry = new DefaultTypeHandlerRegistry();

  private Connection connection;
  private Statement statement;
  private DbResultSet resultSet;
  private boolean strictColumnCheck;


  public DbSelectDataSource(DataSource dataSource, String sql)
  {
    this.dataSource = Assert.notNull(dataSource);
    this.sql = Assert.notNull(sql);
  }


  /**
   * Changes the type handler registry to a custom implementation. By default
   * the standard SQL types are supported.
   * 
   * @param registry a user-defined type handler registry.
   */
  public void setTypeHandlerRegistry(TypeHandlerRegistry registry)
  {
    this.typeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * @return the registry for {@link TypeHandler} instances. Custom
   *         {@link TypeHandler} instances can be added to the returned registry
   *         (each {@link DbSelectDataSource} has its own registry instance).
   */
  public TypeHandlerRegistry getTypeHandlerRegistry()
  {
    return typeHandlerRegistry;
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
    ResultSet rs = getStatement().executeQuery(sql);
    resultSet = new DbResultSet(rs, typeHandlerRegistry, strictColumnCheck);
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
