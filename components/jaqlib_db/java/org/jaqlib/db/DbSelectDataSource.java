package org.jaqlib.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

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
public class DbSelectDataSource extends AbstractDbDataSource
{

  private String sql;

  private boolean strictColumnCheck = Defaults.getStrictColumnCheck();
  private DbResultSet resultSet;


  public DbSelectDataSource(DataSource dataSource, String sql)
  {
    super(dataSource);
    setSql(sql);
  }


  public void setSql(String sql)
  {
    this.sql = Assert.notNull(sql);
  }


  public String getSql()
  {
    return sql;
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
  void closeAfterQuery()
  {
    closeResultSet();
    super.closeAfterQuery();
  }


  public DbResultSet execute(List<?> prepStmtParameters) throws SQLException
  {
    if (prepStmtParameters.isEmpty())
    {
      resultSet = executeStatement();
    }
    else
    {
      resultSet = executePreparedStatement(prepStmtParameters);
    }

    return resultSet;
  }


  private DbResultSet executePreparedStatement(List<?> prepStmtParameters)
      throws SQLException
  {
    log.fine("Executing prepared SQL SELECT statement: " + getSql());

    PreparedStatement stmt = getPreparedStatement(getSql());
    setParameters(stmt, prepStmtParameters);
    prepStmtParameters.clear();

    final ResultSet rs = stmt.executeQuery();
    return new DbResultSet(rs, getSqlTypeHandlerRegistry(), strictColumnCheck);
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
    return new DbResultSet(rs, getSqlTypeHandlerRegistry(), strictColumnCheck);
  }


  @Override
  public String toString()
  {
    return "[SQL: " + sql + "]";
  }


}
