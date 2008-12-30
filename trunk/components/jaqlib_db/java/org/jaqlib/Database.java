package org.jaqlib;

import javax.sql.DataSource;

import org.jaqlib.db.BeanConventionMappingRetrievalStrategy;
import org.jaqlib.db.BeanFactory;
import org.jaqlib.db.BeanMapping;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.DefaultBeanFactory;
import org.jaqlib.db.MappingRetrievalStrategy;
import org.jaqlib.db.java.typehandler.DefaultJavaTypeHandlerRegistry;
import org.jaqlib.db.java.typehandler.JavaTypeHandler;
import org.jaqlib.db.java.typehandler.JavaTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.DefaultSqlTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;

/**
 * <p>
 * Helper class that builds objects for executing queries against databases.
 * This class provides static helper methods but can also be instantiated to
 * make the creation of {@link DbSelectDataSource} and {@link BeanMapping}
 * objects more comfortable. <br>
 * </p>
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * @author Werner Fragner
 */
public class Database
{

  // static fields

  private static MappingRetrievalStrategy defaultMappingRetrievalStrategy = new BeanConventionMappingRetrievalStrategy();

  // mandatory fields

  private final DataSource dataSource;

  // optional / configurable fields

  private MappingRetrievalStrategy mappingRetrievalStrategy = defaultMappingRetrievalStrategy;
  private JavaTypeHandlerRegistry javaTypeHandlerRegistry = new DefaultJavaTypeHandlerRegistry();
  private SqlTypeHandlerRegistry sqlTypeHandlerRegistry = new DefaultSqlTypeHandlerRegistry();
  private BeanFactory beanFactory = DefaultBeanFactory.INSTANCE;
  private boolean strictColumnCheck = false;


  /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   */
  public Database(DataSource dataSource)
  {
    this.dataSource = Assert.notNull(dataSource);
  }


  /**
   * Sets the user-defined mapping retrieval strategy for this database
   * instance. By default the {@link BeanConventionMappingRetrievalStrategy} is
   * used to retrieve the mappings between database columns and Java bean
   * instance fields. <br>
   * This method can be used if another mapping strategy than using bean naming
   * conventions should be applied.
   * 
   * @param strategy a user-defined strategy how to map SELECT statement results
   *          to the fields of a given bean.
   */
  public void setMappingRetrievalStrategy(MappingRetrievalStrategy strategy)
  {
    this.mappingRetrievalStrategy = Assert.notNull(mappingRetrievalStrategy);
  }


  /**
   * Registers the given custom SQL type handler with the given SQL data type.
   * 
   * @param sqlDataType a SQL data type as defined at {@link java.sql.Types}.
   * @param typeHandler a not null type handler.
   */
  public void registerSqlTypeHandler(int sqlDataType, SqlTypeHandler typeHandler)
  {
    sqlTypeHandlerRegistry.registerTypeHandler(sqlDataType, typeHandler);
  }


  /**
   * Changes the SQL type handler registry to a custom implementation. By
   * default the standard SQL types are supported.
   * 
   * @param registry a user-defined SQL type handler registry.
   */
  public void setSqlTypeHandlerRegistry(SqlTypeHandlerRegistry registry)
  {
    this.sqlTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * Registers a custom java type handler with a given java type.
   * 
   * @param fieldType a not null java type.
   * @param typeHandler a not null custom java type handler.
   */
  public void registerJavaTypeHandler(Class<?> fieldType,
      JavaTypeHandler typeHandler)
  {
    javaTypeHandlerRegistry.registerTypeHandler(fieldType, typeHandler);
  }


  /**
   * Changes the java type handler registry to a custom implementation. By
   * default no type handlers are available.
   * 
   * @param registry a user-defined java type handler registry.
   */
  public void setJavaTypeHandlerRegistry(JavaTypeHandlerRegistry registry)
  {
    this.javaTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * Sets a custom bean factory for creating bean instances.
   * 
   * @param beanFactory a not null bean factory.
   */
  public void setBeanFactory(BeanFactory beanFactory)
  {
    this.beanFactory = Assert.notNull(beanFactory);
  }


  /**
   * Enables/disables strict checking if a field in a Java bean does not exist
   * in the SELECT statement. If strict column check is enabled then an
   * exception is thrown if a Java bean field does exist in the SELECT
   * statement. If strict column check is disabled (DEFAULT) then an INFO log
   * message is issued and the field is ignored (= is not set). If these INFO
   * messages should not be issued then the JDK logger for
   * 'org.jaqlib.query.db.AbstractJaqLibOrMapper' must be disabled (see <a
   * href="
   * http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html">Java
   * Logging</a>).
   * 
   * @param strictColumnCheck enable/disable strict column check.
   */
  public void setStrictColumnCheck(boolean strictColumnCheck)
  {
    this.strictColumnCheck = strictColumnCheck;
  }


  /**
   * 
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement.
   * @return an object describing where to store a SELECT statement result.
   */
  public <T> BeanMapping<T> getBeanMapping(Class<T> beanClass)
  {
    BeanMapping<T> mapping = getBeanMapping(mappingRetrievalStrategy, beanClass);
    mapping.setBeanFactory(beanFactory);
    mapping.setJavaTypeHandlerRegistry(javaTypeHandlerRegistry);
    return mapping;
  }


  /**
   * @param sql a not null SELECT statement.
   * @return an object representing the source for a database query.
   */
  public DbSelectDataSource getSelectDataSource(String sql)
  {
    DbSelectDataSource ds = new DbSelectDataSource(dataSource, sql);
    ds.setSqlTypeHandlerRegistry(sqlTypeHandlerRegistry);
    ds.setStrictColumnCheck(strictColumnCheck);
    return ds;
  }


  /**
   * Sets the default user-defined mapping retrieval strategy. By default the
   * {@link BeanConventionMappingRetrievalStrategy} is used to retrieve the
   * mappings between database columns and Java bean instance fields.<br>
   * This method can be used if another mapping strategy than using bean naming
   * conventions should be applied.
   * 
   * @param strategy a user-defined strategy how to map SELECT statement results
   *          to the fields of a given bean.
   */
  public static void setDefaultMappingRetrievalStrategy(
      MappingRetrievalStrategy strategy)
  {
    defaultMappingRetrievalStrategy = Assert.notNull(strategy);
  }


  /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param sql a not null SELECT statement.
   * @return an object representing the source for a database query.
   */
  public static DbSelectDataSource getSelectDataSource(DataSource dataSource,
      String sql)
  {
    return new Database(dataSource).getSelectDataSource(sql);
  }


  /**
   * Creates a {@link BeanMapping} instance by using the bean properties of the
   * given class. Bean properties must have a valid get and set method in order
   * to be in the returned {@link BeanMapping}.
   * 
   * @param <T> the type of the result bean.
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement. Additionally this class is used to retrieve the
   *          bean properties for storing the result of the SELECT statement.
   * @return an object describing where to store a SELECT statement result.
   */
  public static <T> BeanMapping<T> getDefaultBeanMapping(Class<T> beanClass)
  {
    return getBeanMapping(defaultMappingRetrievalStrategy, beanClass);
  }


  /**
   * @param mappingStrategy a user-defined strategy how to map SELECT statement
   *          results to the fields of the given bean.
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement.
   * @return an object describing where to store a SELECT statement result.
   */
  public static <T> BeanMapping<T> getBeanMapping(
      MappingRetrievalStrategy mappingStrategy, Class<T> beanClass)
  {
    BeanMapping<T> result = new BeanMapping<T>(beanClass);
    mappingStrategy.addMappings(beanClass, result);
    return result;
  }

}
