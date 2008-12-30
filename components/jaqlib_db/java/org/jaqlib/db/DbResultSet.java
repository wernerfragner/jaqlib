package org.jaqlib.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.jaqlib.db.java.typehandler.JavaTypeHandler;
import org.jaqlib.db.java.typehandler.JavaTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;
import org.jaqlib.util.db.DbUtil;

/**
 * @author Werner Fragner
 */
public class DbResultSet
{

  public static final Object NO_RESULT = new Object();
  private static final Logger LOG = Logger.getLogger(DbResultSet.class
      .getName());

  private final SqlTypeHandlerRegistry sqlTypeHandlerRegistry;
  private final JavaTypeHandlerRegistry javaTypeHandlerRegistry;

  private final ResultSet resultSet;
  private final DbResultSetMetaData resultSetMetaData;
  private final boolean strictColumnCheck;


  public DbResultSet(ResultSet resultSet,
      SqlTypeHandlerRegistry sqlTypeHandlerRegistry,
      JavaTypeHandlerRegistry javaTypeHandlerRegistry, boolean strictColumnCheck)
      throws SQLException
  {
    this.resultSet = Assert.notNull(resultSet);
    this.resultSetMetaData = new DbResultSetMetaData(resultSet.getMetaData());
    this.sqlTypeHandlerRegistry = Assert.notNull(sqlTypeHandlerRegistry);
    this.javaTypeHandlerRegistry = Assert.notNull(javaTypeHandlerRegistry);
    this.strictColumnCheck = strictColumnCheck;
  }


  public ResultSet getResultSet()
  {
    return resultSet;
  }


  public boolean hasColumn(String columnName) throws SQLException
  {
    return resultSetMetaData.hasColumn(columnName);
  }


  public void close()
  {
    DbUtil.close(resultSet);
  }


  public boolean next() throws SQLException
  {
    return resultSet.next();
  }


  public Object getObject(int columnDataType, String columnName)
      throws SQLException
  {
    if (strictColumnCheck || hasColumn(columnName))
    {
      return getSqlTypeHandler(columnDataType).getObject(resultSet, columnName);
    }
    else
    {
      LOG.info("SELECT statement does not contain colum '" + columnName
          + "'. Column is ignored.");
      return NO_RESULT;
    }
  }


  public Object getObject(int columnDataType, int columnIndex)
      throws SQLException
  {
    return getSqlTypeHandler(columnDataType).getObject(resultSet, columnIndex);
  }


  private SqlTypeHandler getSqlTypeHandler(int columnDataType)
  {
    return sqlTypeHandlerRegistry.getTypeHandler(columnDataType);
  }


  public Object applyJavaTypeHandler(Class<?> fieldType, Object value)
  {
    return getJavaTypeHandler(fieldType).getObject(value);
  }


  private JavaTypeHandler getJavaTypeHandler(Class<?> fieldType)
  {
    return javaTypeHandlerRegistry.getTypeHandler(fieldType);
  }

}
