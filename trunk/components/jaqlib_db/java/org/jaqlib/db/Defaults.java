package org.jaqlib.db;

import org.jaqlib.db.java.typehandler.DefaultJavaTypeHandlerRegistry;
import org.jaqlib.db.java.typehandler.JavaTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.DefaultSqlTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;

/**
 * Static helper class that holds default infrastructure component instances.
 * 
 * @author Werner Fragner
 */
public class Defaults
{

  private Defaults()
  {
    throw new UnsupportedOperationException();
  }


  private static BeanFactory beanFactory = DefaultBeanFactory.INSTANCE;
  private static MappingRetrievalStrategy mappingRetrievalStrategy = new BeanConventionMappingRetrievalStrategy();
  private static JavaTypeHandlerRegistry javaTypeHandlerRegistry = new DefaultJavaTypeHandlerRegistry();
  private static SqlTypeHandlerRegistry sqlTypeHandlerRegistry = new DefaultSqlTypeHandlerRegistry();
  private static boolean strictColumnCheck = false;


  public static BeanFactory getBeanFactory()
  {
    return beanFactory;
  }


  public static void setBeanFactory(BeanFactory beanFactory)
  {
    Defaults.beanFactory = Assert.notNull(beanFactory);
  }


  public static MappingRetrievalStrategy getMappingRetrievalStrategy()
  {
    return mappingRetrievalStrategy;
  }


  public static void setMappingRetrievalStrategy(
      MappingRetrievalStrategy mappingRetrievalStrategy)
  {
    Defaults.mappingRetrievalStrategy = Assert
        .notNull(mappingRetrievalStrategy);
  }


  public static JavaTypeHandlerRegistry getJavaTypeHandlerRegistry()
  {
    return javaTypeHandlerRegistry;
  }


  public static void setJavaTypeHandlerRegistry(
      JavaTypeHandlerRegistry javaTypeHandlerRegistry)
  {
    Defaults.javaTypeHandlerRegistry = Assert.notNull(javaTypeHandlerRegistry);
  }


  public static SqlTypeHandlerRegistry getSqlTypeHandlerRegistry()
  {
    return sqlTypeHandlerRegistry;
  }


  public static void setSqlTypeHandlerRegistry(
      SqlTypeHandlerRegistry sqlTypeHandlerRegistry)
  {
    Defaults.sqlTypeHandlerRegistry = Assert.notNull(sqlTypeHandlerRegistry);
  }


  public static void setStrictColumnCheck(boolean strictColumnCheck)
  {
    Defaults.strictColumnCheck = strictColumnCheck;
  }


  public static boolean getStrictColumnCheck()
  {
    return strictColumnCheck;
  }

}
