package org.jaqlib.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.jaqlib.core.DataSourceQueryException;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;
import org.jaqlib.util.DbUtil;
import org.jaqlib.util.LogUtil;

/**
 * @author Werner Fragner
 */
public class DbResultSet implements DsResultSet
{

  public static final Object NO_RESULT = new Object();
  private final Logger log = LogUtil.getLogger(this);

  private final SqlTypeHandlerRegistry sqlTypeHandlerRegistry;

  private final ResultSet resultSet;
  private final DbResultSetMetaData resultSetMetaData;
  private final boolean strictColumnCheck;


  public DbResultSet(ResultSet resultSet,
      SqlTypeHandlerRegistry sqlTypeHandlerRegistry, boolean strictColumnCheck)
      throws SQLException
  {
    this.resultSet = Assert.notNull(resultSet);
    this.resultSetMetaData = new DbResultSetMetaData(resultSet.getMetaData());
    this.sqlTypeHandlerRegistry = Assert.notNull(sqlTypeHandlerRegistry);
    this.strictColumnCheck = strictColumnCheck;
  }


  public boolean hasColumn(String columnName) throws SQLException
  {
    return resultSetMetaData.hasColumn(columnName);
  }


  public void close()
  {
    DbUtil.close(resultSet);
  }


  public boolean next()
  {
    try
    {
      return resultSet.next();
    }
    catch (SQLException ex)
    {
      throw toDataSourceQueryException(ex);
    }
  }


  public Object getObject(int sqlDataType, String columnLabel)
  {
    try
    {
      if (strictColumnCheck || hasColumn(columnLabel))
      {
        return getSqlTypeHandler(sqlDataType).getObject(resultSet, columnLabel);
      }
      else
      {
        log.info("SELECT statement does not contain colum '" + columnLabel
            + "'. Column is ignored.");
        return NO_RESULT;
      }
    }
    catch (SQLException ex)
    {
      throw toDataSourceQueryException(ex);
    }
  }


  public Object getObject(int sqlDataType, int columnIndex)
  {
    try
    {
      return getSqlTypeHandler(sqlDataType).getObject(resultSet, columnIndex);
    }
    catch (SQLException ex)
    {
      throw toDataSourceQueryException(ex);
    }
  }


  private DataSourceQueryException toDataSourceQueryException(SQLException e)
  {
    DataSourceQueryException ex = new DataSourceQueryException(e);
    ex.setStackTrace(e.getStackTrace());
    return ex;
  }


  private SqlTypeHandler getSqlTypeHandler(int sqlDataType)
  {
    return sqlTypeHandlerRegistry.getTypeHandler(sqlDataType);
  }


}
