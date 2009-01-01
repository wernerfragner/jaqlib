package org.jaqlib.db;

import org.jaqlib.db.java.typehandler.DefaultJavaTypeHandlerRegistry;
import org.jaqlib.db.java.typehandler.JavaTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.DefaultSqlTypeHandlerRegistry;
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


  private static BeanFactory beanFactory = DefaultBeanFactory.INSTANCE;
  private static MappingStrategy mappingStrategy = new BeanConventionMappingStrategy();
  private static JavaTypeHandlerRegistry javaTypeHandlerRegistry = new DefaultJavaTypeHandlerRegistry();
  private static SqlTypeHandlerRegistry sqlTypeHandlerRegistry = new DefaultSqlTypeHandlerRegistry();
  private static boolean strictColumnCheck = false;


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
