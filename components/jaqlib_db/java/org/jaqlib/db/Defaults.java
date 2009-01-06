package org.jaqlib.db;

import org.jaqlib.db.java.typehandler.DefaultJavaTypeHandlerRegistry;
import org.jaqlib.db.java.typehandler.JavaTypeHandler;
import org.jaqlib.db.java.typehandler.JavaTypeHandlerRegistry;
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
public class Defaults
{

  /**
   * This class is not intended to be instantiated.
   */
  private Defaults()
  {
    throw new UnsupportedOperationException();
  }


  private static BeanFactory beanFactory;
  private static MappingStrategy mappingStrategy;
  private static JavaTypeHandlerRegistry javaTypeHandlerRegistry;
  private static SqlTypeHandlerRegistry sqlTypeHandlerRegistry;
  private static boolean strictColumnCheck;


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
    beanFactory = new DefaultBeanFactory();
    mappingStrategy = new BeanConventionMappingStrategy();
    javaTypeHandlerRegistry = new DefaultJavaTypeHandlerRegistry();
    sqlTypeHandlerRegistry = new DefaultSqlTypeHandlerRegistry();
    strictColumnCheck = false;
  }


  /**
   * @return the default bean factory.
   */
  public static BeanFactory getBeanFactory()
  {
    return beanFactory;
  }


  /**
   * Sets the default bean factory.<br>
   * <b>NOTE: this method changes the default bean factory for the whole
   * application! Use with care.</b>
   * 
   * @param beanFactory a not null bean factory.
   */
  public static void setBeanFactory(BeanFactory beanFactory)
  {
    Defaults.beanFactory = Assert.notNull(beanFactory);
  }


  /**
   * @return the default bean mapping strategy.
   */
  public static MappingStrategy getMappingStrategy()
  {
    return mappingStrategy;
  }


  /**
   * Sets the default bean mapping strategy.<br>
   * <b>NOTE: this method changes the default bean mapping strategy for the
   * whole application! Use with care.</b>
   * 
   * @param strategy a not null strategy.
   */
  public static void setMappingStrategy(MappingStrategy strategy)
  {
    Defaults.mappingStrategy = Assert.notNull(strategy);
  }


  /**
   * @return the default Java type handler registry.
   */
  public static JavaTypeHandlerRegistry getJavaTypeHandlerRegistry()
  {
    return javaTypeHandlerRegistry;
  }


  /**
   * Sets the default Java type handler registry.<br>
   * <b>NOTE: this method changes the default Java type handler registry for the
   * whole application! Use with care.</b>
   * 
   * @param registry a not null registry.
   */
  public static void setJavaTypeHandlerRegistry(JavaTypeHandlerRegistry registry)
  {
    Defaults.javaTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * See {@link JavaTypeHandlerRegistry#registerTypeHandler(JavaTypeHandler)} .
   */
  public static void registerJavaTypeHandler(JavaTypeHandler typeHandler)
  {
    getJavaTypeHandlerRegistry().registerTypeHandler(typeHandler);
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
    Defaults.sqlTypeHandlerRegistry = Assert.notNull(registry);
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


  /**
   * Sets the default value for 'strictColumnCheck'.<br>
   * <b>NOTE: this method changes the default value for the whole application!
   * Use with care.</b>
   * 
   * @param strictColumnCheck
   */
  public static void setStrictColumnCheck(boolean strictColumnCheck)
  {
    Defaults.strictColumnCheck = strictColumnCheck;
  }


  /**
   * @return true if all desired bean fields must be present in the result of
   *         the SQL SELECT statement.
   */
  public static boolean getStrictColumnCheck()
  {
    return strictColumnCheck;
  }

}
