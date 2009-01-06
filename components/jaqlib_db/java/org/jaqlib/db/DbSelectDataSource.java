package org.jaqlib.db;

import java.sql.ResultSet;
import java.sql.SQLException;

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

  private final String sql;

  private boolean strictColumnCheck = Defaults.getStrictColumnCheck();
  private DbResultSet resultSet;


  public DbSelectDataSource(DataSource dataSource, String sql)
  {
    super(dataSource);
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


  @Override
  public void close()
  {
    close(resultSet);
    resultSet = null;

    super.close();
  }


  public DbResultSet execute() throws SQLException
  {
    log.fine("Executing SQL SELECT statement: " + getSql());

    final ResultSet rs = getStatement().executeQuery(getSql());
    resultSet = new DbResultSet(rs, getSqlTypeHandlerRegistry(),
        strictColumnCheck);
    return resultSet;
  }


  @Override
  public String toString()
  {
    return "[SQL: " + sql + "]";
  }

}
