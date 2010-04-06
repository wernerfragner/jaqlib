package org.jaqlib.db;

import org.jaqlib.db.sql.typehandler.DefaultSqlTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;

/**
 * Static helper class that holds default infrastructure component instances and
 * global properties.<br>
 * <b>NOTE: Changes to these components/properties have an effect on the entire
 * application. Use with care!</b>
 * 
 * @author Werner Fragner
 */
public class DbDefaults
{

  /**
   * This class is not intended to be instantiated.
   */
  private DbDefaults()
  {
    throw new UnsupportedOperationException();
  }


  private static SqlTypeHandlerRegistry sqlTypeHandlerRegistry;


  /**
   * Initializes the static fields with default values.
   */
  static
  {
    reset();
  }


  /**
   * Resets the static fields to their original default values.
   */
  public static void reset()
  {
    sqlTypeHandlerRegistry = new DefaultSqlTypeHandlerRegistry();
  }


  /**
   * @return the default SQL type handler registry.
   */
  public static SqlTypeHandlerRegistry getSqlTypeHandlerRegistry()
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
  public static void setSqlTypeHandlerRegistry(SqlTypeHandlerRegistry registry)
  {
    DbDefaults.sqlTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * See {@link SqlTypeHandlerRegistry#registerTypeHandler(int, SqlTypeHandler)}
   * .
   */
  public static void registerSqlTypeHandler(int sqlDataType,
      SqlTypeHandler typeHandler)
  {
    getSqlTypeHandlerRegistry().registerTypeHandler(sqlDataType, typeHandler);
  }


}
