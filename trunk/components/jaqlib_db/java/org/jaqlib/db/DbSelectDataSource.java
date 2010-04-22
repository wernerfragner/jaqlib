package org.jaqlib.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.jaqlib.core.DataSourceQueryException;
import org.jaqlib.core.SelectDataSource;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;

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
public class DbSelectDataSource extends AbstractDbDataSource implements
    SelectDataSource
{

  private String sql;

  private boolean strictFieldCheck = DbDefaults.INSTANCE.getStrictFieldCheck();
  private DbResultSet resultSet;

  private final List<Object> prepStmtParameters = new ArrayList<Object>();
  private String sqlWhereCondition;


  public DbSelectDataSource(DataSource dataSource, String sql)
  {
    super(dataSource);
    setSql(sql);
  }


  public void setSql(String sql)
  {
    this.sql = Assert.notNull(sql);
  }


  /**
   * Sets the SQL WHERE condition. <b>NOTE: this WHERE condition is simply
   * appended to the given SQL statement using the WHERE keyword (e.g.
   * sql="SELECT name FROM user", sqlWhereCondition="name like 'w%'" results in
   * the SQL statement "SELECT name FROM user WHERE name like 'w%'"). So do not
   * add a WHERE condition in the SQL statement when using this method!</b>
   * 
   * @param sqlWhereCondition an optional SQL WHERE condition.
   */
  public void setSqlWhereCondition(String sqlWhereCondition)
  {
    this.sqlWhereCondition = sqlWhereCondition;
  }


  public String getSql()
  {
    if (isEmpty(sqlWhereCondition))
    {
      return sql;
    }
    else
    {
      return sql + " WHERE " + sqlWhereCondition;
    }
  }


  private boolean isEmpty(String str)
  {
    return str == null || str.trim().length() < 1;
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
    this.strictFieldCheck = strictColumnCheck;
  }


  public void addPreparedStatementParameter(Object param)
  {
    prepStmtParameters.add(param);
  }


  private void closeResultSet()
  {
    close(resultSet);
    resultSet = null;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void close()
  {
    closeResultSet();
    super.close();
  }


  @Override
  public void closeAfterQuery()
  {
    closeResultSet();
    super.closeAfterQuery();
  }


  public DbResultSet execute()
  {
    try
    {
      if (prepStmtParameters.isEmpty())
      {
        resultSet = executeStatement();
      }
      else
      {
        resultSet = executePreparedStatement();
      }

      return resultSet;
    }
    catch (SQLException ex)
    {
      throw toDataSourceQueryException(ex);
    }
  }


  private DataSourceQueryException toDataSourceQueryException(SQLException ex)
  {
    DataSourceQueryException e = new DataSourceQueryException(ex);
    e.setStackTrace(ex.getStackTrace());
    return e;
  }


  private DbResultSet executePreparedStatement() throws SQLException
  {
    log.fine("Executing prepared SQL SELECT statement: " + getSql());

    PreparedStatement stmt = getPreparedStatement(getSql());
    setParameters(stmt, prepStmtParameters);
    prepStmtParameters.clear();

    final ResultSet rs = stmt.executeQuery();
    return new DbResultSet(rs, getSqlTypeHandlerRegistry(), strictFieldCheck);
  }


  private void setParameters(PreparedStatement stmt, List<?> prepStmtParameters)
      throws SQLException
  {
    for (int i = 0; i < prepStmtParameters.size(); i++)
    {
      stmt.setObject(i + 1, prepStmtParameters.get(i));
    }
  }


  private DbResultSet executeStatement() throws SQLException
  {
    log.fine("Executing SQL SELECT statement: " + getSql());

    final ResultSet rs = getStatement().executeQuery(getSql());
    return new DbResultSet(rs, getSqlTypeHandlerRegistry(), strictFieldCheck);
  }


  @Override
  public String toString()
  {
    return "[SQL: " + getSql() + "]";
  }


}
