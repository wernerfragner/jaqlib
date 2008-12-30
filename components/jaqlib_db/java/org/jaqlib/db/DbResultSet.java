package org.jaqlib.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.jaqlib.db.java.typehandler.BeanFieldTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.DbFieldTypeHandler;
import org.jaqlib.db.sql.typehandler.DbFieldTypeHandlerRegistry;
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

  private final DbFieldTypeHandlerRegistry typeHandlerRegistry;
  private final BeanFieldTypeHandlerRegistry beanFieldTypeHandlerRegistry;

  private final ResultSet resultSet;
  private final DbResultSetMetaData resultSetMetaData;
  private final boolean strictColumnCheck;


  public DbResultSet(ResultSet resultSet,
      DbFieldTypeHandlerRegistry typeHandlerRegistry,
      BeanFieldTypeHandlerRegistry beanFieldTypeHandlerRegistry,
      boolean strictColumnCheck) throws SQLException
  {
    this.resultSet = Assert.notNull(resultSet);
    this.resultSetMetaData = new DbResultSetMetaData(resultSet.getMetaData());
    this.typeHandlerRegistry = Assert.notNull(typeHandlerRegistry);
    this.beanFieldTypeHandlerRegistry = Assert
        .notNull(beanFieldTypeHandlerRegistry);
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
      return getTypeHandler(columnDataType).getObject(resultSet, columnName);
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
    return getTypeHandler(columnDataType).getObject(resultSet, columnIndex);
  }


  private DbFieldTypeHandler getTypeHandler(int columnDataType)
  {
    return typeHandlerRegistry.getTypeHandler(columnDataType);
  }


  public Object applyBeanFieldTypeHandler(Class<?> fieldType, Object value)
  {
    return beanFieldTypeHandlerRegistry.getTypeHandler(fieldType).getValue(
        value);
  }

}
