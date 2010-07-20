package org.jaqlib.db;

import org.jaqlib.core.DefaultsDelegate;
import org.jaqlib.db.sql.typehandler.DefaultSqlTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;

/**
 * Static helper class that holds default infrastructure component instances and
 * application-wide properties.<br>
 * <b>NOTE: Changes to these components/properties have an effect on the entire
 * application. Use with care!</b>
 * 
 * @author Werner Fragner
 */
public class DbDefaults extends DefaultsDelegate
{

  /**
   * This class must only be instantiated as singleton.
   */
  private DbDefaults()
  {
  }


  /**
   * Singleton instance of this class.
   */
  public static final DbDefaults INSTANCE = new DbDefaults();

  private SqlTypeHandlerRegistry sqlTypeHandlerRegistry;
  private boolean autoCloseConnection = false;
  private boolean autoClosePreparedStatement = true;


  /**
   * Resets all defaults to their initial values.
   */
  static
  {
    INSTANCE.reset();
  }


  /**
   * Resets all defaults to their initial values.
   */
  @Override
  public void reset()
  {
    super.reset();

    sqlTypeHandlerRegistry = new DefaultSqlTypeHandlerRegistry();
    autoCloseConnection = false;
    autoClosePreparedStatement = true;
  }


  /**
   * @return the default SQL type handler registry.
   */
  public SqlTypeHandlerRegistry getSqlTypeHandlerRegistry()
  {
    return sqlTypeHandlerRegistry;
  }


  /**
   * Sets the default SQL type handler registry.<br>
   * <b>NOTE: this method changes the default SQL type handler registry for the
   * whole application! Use with care.</b>
   * 
   * @param registry a not null registry.
   */
  public void setSqlTypeHandlerRegistry(SqlTypeHandlerRegistry registry)
  {
    sqlTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * See {@link SqlTypeHandlerRegistry#registerTypeHandler(int, SqlTypeHandler)}
   * .
   */
  public void registerSqlTypeHandler(int sqlDataType, SqlTypeHandler typeHandler)
  {
    getSqlTypeHandlerRegistry().registerTypeHandler(sqlDataType, typeHandler);
  }


  /**
   * See {@link AbstractDbDataSource#setAutoCloseConnection(boolean)}.
   */
  public void setAutoCloseConnection(boolean autoCloseConnection)
  {
    this.autoCloseConnection = autoCloseConnection;
  }


  /**
   * See {@link AbstractDbDataSource#isAutoCloseConnection()}.
   */
  public boolean isAutoCloseConnection()
  {
    return autoCloseConnection;
  }


  /**
   * See {@link AbstractDbDataSource#setAutoClosePreparedStatement(boolean)}.
   */
  public void setAutoClosePreparedStatement(boolean value)
  {
    this.autoClosePreparedStatement = value;
  }


  /**
   * See {@link AbstractDbDataSource#isAutoClosePreparedStatement()}.
   */
  public boolean isAutoClosePreparedStatement()
  {
    return autoClosePreparedStatement;
  }

}
