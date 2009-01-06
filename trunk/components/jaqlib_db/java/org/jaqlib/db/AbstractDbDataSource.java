package org.jaqlib.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;
import org.jaqlib.util.DbUtil;
import org.jaqlib.util.LogUtil;

/**
 * @author Werner Fragner
 */
public abstract class AbstractDbDataSource
{

  protected final Logger log = LogUtil.getLogger(this);

  private final DataSource dataSource;

  private SqlTypeHandlerRegistry sqlTypeHandlerRegistry = Defaults
      .getSqlTypeHandlerRegistry();

  private Connection connection;
  private Statement statement;
  private final Map<String, PreparedStatement> prepStatements = CollectionUtil
      .newDefaultMap();


  public AbstractDbDataSource(DataSource dataSource)
  {
    this.dataSource = Assert.notNull(dataSource);
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


  protected SqlTypeHandlerRegistry getSqlTypeHandlerRegistry()
  {
    return sqlTypeHandlerRegistry;
  }


  protected void commit() throws SQLException
  {
    if (connection != null)
    {
      connection.commit();
    }
  }


  public void close()
  {
    DbUtil.close(statement);
    statement = null;
    closePreparedStatements();
    prepStatements.clear();
    DbUtil.close(connection);
    connection = null;
  }


  private void closePreparedStatements()
  {
    for (PreparedStatement stmt : prepStatements.values())
    {
      DbUtil.close(stmt);
    }
  }


  protected Statement getStatement() throws SQLException
  {
    if (statement == null)
    {
      log.fine("Creating SQL statement");

      statement = getConnection().createStatement();
    }
    return statement;
  }


  protected PreparedStatement getPreparedStatement(String sql)
      throws SQLException
  {
    PreparedStatement stmt = prepStatements.get(sql);
    if (stmt == null)
    {
      stmt = getConnection().prepareStatement(sql);
      prepStatements.put(sql, stmt);
    }
    return stmt;
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


  protected void close(DbResultSet rs)
  {
    if (rs != null)
    {
      rs.close();
    }
  }


  protected DataSourceQueryException handleSqlException(SQLException sqle)
  {
    DataSourceQueryException e = new DataSourceQueryException(sqle);
    e.setStackTrace(sqle.getStackTrace());
    return e;
  }


  protected void appendWhereClause(StringBuilder sql, String whereClause)
  {
    if (shouldAppendWhereClause(whereClause))
    {
      sql.append(" WHERE ");
      sql.append(whereClause);
    }
  }


  private boolean shouldAppendWhereClause(String whereClause)
  {
    return (whereClause != null && whereClause.trim().length() > 0);
  }

}
