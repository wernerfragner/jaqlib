package org.jaqlib.query.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jaqlib.util.Assert;
import org.jaqlib.util.bean.typehandler.BeanFieldTypeHandler;
import org.jaqlib.util.bean.typehandler.BeanFieldTypeHandlerRegistry;
import org.jaqlib.util.bean.typehandler.DefaultBeanFieldTypeHandlerRegistry;
import org.jaqlib.util.db.DbUtil;
import org.jaqlib.util.db.typehandler.DbFieldTypeHandler;
import org.jaqlib.util.db.typehandler.DbFieldTypeHandlerRegistry;
import org.jaqlib.util.db.typehandler.DefaultDbFieldTypeHandlerRegistry;

/**
 * <p>
 * Represents a SELECT statement on a given JDBC connection. The result mapping
 * can be customized using following methods:
 * <ul>
 * <li>{@link #setStrictColumnCheck(boolean)}</li>
 * <li>{@link #setDbFieldTypeHandlerRegistry(DbFieldTypeHandlerRegistry)}</li>
 * <li>{@link #registerDbFieldTypeHandler(int, DbFieldTypeHandler)}</li>
 * <li>{@link #setBeanFieldTypeHandlerRegistry(BeanFieldTypeHandlerRegistry)}</li>
 * <li>{@link #registerBeanFieldTypeHandler(Class, BeanFieldTypeHandler)}</li>
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

  private boolean strictColumnCheck;

  private DbFieldTypeHandlerRegistry dbFieldtypeHandlerRegistry = new DefaultDbFieldTypeHandlerRegistry();
  private BeanFieldTypeHandlerRegistry beanFieldTypeHandlerRegistry = new DefaultBeanFieldTypeHandlerRegistry();

  private Connection connection;
  private Statement statement;
  private DbResultSet resultSet;


  public DbSelectDataSource(DataSource dataSource, String sql)
  {
    this.dataSource = Assert.notNull(dataSource);
    this.sql = Assert.notNull(sql);
  }


  /**
   * Registers the given custom type handler with the given DB data type.
   * 
   * @param dbDataType a DB data type as defined at {@link java.sql.Types}.
   * @param typeHandler a not null type handler.
   */
  public void registerDbFieldTypeHandler(int dbDataType,
      DbFieldTypeHandler typeHandler)
  {
    dbFieldtypeHandlerRegistry.registerTypeHandler(dbDataType, typeHandler);
  }


  /**
   * Changes the type handler registry to a custom implementation. By default
   * the standard SQL types are supported.
   * 
   * @param registry a user-defined type handler registry.
   */
  public void setDbFieldTypeHandlerRegistry(DbFieldTypeHandlerRegistry registry)
  {
    this.dbFieldtypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * Registers a custom bean field type handler with a given bean field type.
   * 
   * @param fieldType a not null bean field type.
   * @param typeHandler a not null custom bean field type handler.
   */
  public void registerBeanFieldTypeHandler(Class<?> fieldType,
      BeanFieldTypeHandler typeHandler)
  {
    beanFieldTypeHandlerRegistry.registerTypeHandler(fieldType, typeHandler);
  }


  /**
   * Changes the bean field type handler registry to a custom implementation. By
   * default no type handles are available.
   * 
   * @param registry a user-defined bean field type handler registry.
   */
  public void setBeanFieldTypeHandlerRegistry(
      BeanFieldTypeHandlerRegistry registry)
  {
    this.beanFieldTypeHandlerRegistry = registry;
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
    resultSet = new DbResultSet(rs, dbFieldtypeHandlerRegistry,
        beanFieldTypeHandlerRegistry, strictColumnCheck);
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
