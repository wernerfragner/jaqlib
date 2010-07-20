package org.jaqlib;

import javax.sql.DataSource;

import org.jaqlib.core.DataSourceQueryException;
import org.jaqlib.core.bean.BeanConventionMappingStrategy;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.core.bean.JavaTypeHandlerRegistry;
import org.jaqlib.db.DbDefaults;
import org.jaqlib.db.DbDeleteDataSource;
import org.jaqlib.db.DbInsertDataSource;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.DbUpdateDataSource;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.util.Assert;

/**
 * <p>
 * Note that instead of using this class the required data classes (
 * {@link DbSelectDataSource}, {@link DbInsertDataSource}, {@link BeanMapping},
 * ...) can be instantiated directly by using the <tt>new</tt> operator.
 * </p>
 * <p>
 * Helper class that builds objects for executing queries against databases.
 * This class provides static helper methods but can also be instantiated to
 * make the creation of {@link DbSelectDataSource}, {@link DbInsertDataSource},
 * ... and {@link BeanMapping} objects more comfortable. <br>
 * </p>
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * @author Werner Fragner
 */
public class Database
{

  // mandatory fields

  private final DataSource dataSource;

  // optional / configurable fields

  private BeanMappingStrategy mappingStrategy = DbDefaults.INSTANCE
      .getBeanMappingStrategy();
  private JavaTypeHandlerRegistry javaTypeHandlerRegistry = DbDefaults.INSTANCE
      .getJavaTypeHandlerRegistry();
  private SqlTypeHandlerRegistry sqlTypeHandlerRegistry = DbDefaults.INSTANCE
      .getSqlTypeHandlerRegistry();
  private BeanFactory beanFactory = DbDefaults.INSTANCE.getBeanFactory();
  private boolean strictColumnCheck = DbDefaults.INSTANCE.getStrictFieldCheck();


  /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   */
  public Database(DataSource dataSource)
  {
    this.dataSource = Assert.notNull(dataSource);
  }


  /**
   * Sets a custom mapping strategy. By default the
   * {@link BeanConventionMappingStrategy} is used to retrieve the mappings
   * between database columns and Java bean instance fields.<br>
   * This method can be used if another mapping strategy than using bean naming
   * conventions should be applied.
   * 
   * @param strategy a custom strategy how to map SELECT statement results to
   *          the fields of a given bean.
   */
  public void setMappingStrategy(BeanMappingStrategy strategy)
  {
    mappingStrategy = Assert.notNull(strategy);
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
   * @param registry a custom SQL type handler registry.
   */
  public void setSqlTypeHandlerRegistry(SqlTypeHandlerRegistry registry)
  {
    this.sqlTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * Registers a custom java type handler.
   * 
   * @param typeHandler a not null custom java type handler.
   */
  public void registerJavaTypeHandler(JavaTypeHandler typeHandler)
  {
    javaTypeHandlerRegistry.registerTypeHandler(typeHandler);
  }


  /**
   * Changes the java type handler registry to a custom implementation. By
   * default no type handlers are available.
   * 
   * @param registry a custom java type handler registry.
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


  /**
   * 
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement.
   * @return an object describing where to store a SELECT statement result.
   */
  public <T> BeanMapping<T> getBeanMapping(Class<? extends T> beanClass)
  {
    BeanMapping<T> mapping = getBeanMapping(mappingStrategy, beanClass);
    mapping.setFactory(beanFactory);
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
   * @param tableName the not null name of the table where to insert the record.
   * @return an object representing the source for a (or multiple) database
   *         insert.
   */
  public DbInsertDataSource getInsertDataSource(String tableName)
  {
    DbInsertDataSource ds = new DbInsertDataSource(dataSource, tableName);
    ds.setSqlTypeHandlerRegistry(sqlTypeHandlerRegistry);
    return ds;
  }


  /**
   * @param tableName the not null name of the table where to insert the record.
   * @param whereClause the where clause of the UPDATE statement without the
   *          WHERE keyword.
   * @return an object representing the source for a (or multiple) database
   *         insert.
   */
  public DbUpdateDataSource getUpdateDataSource(String tableName,
      String whereClause)
  {
    DbUpdateDataSource ds = new DbUpdateDataSource(dataSource, tableName,
        whereClause);
    ds.setSqlTypeHandlerRegistry(sqlTypeHandlerRegistry);
    return ds;
  }


  /**
   * @param tableName the not null name of the table where to insert the record.
   * @return an object representing the source for a (or multiple) database
   *         insert.
   */
  public DbUpdateDataSource getUpdateDataSource(String tableName)
  {
    return getUpdateDataSource(tableName, null);
  }


  /**
   * Gets a datasource for deleting a set of records of a table. Which records
   * should be deleted can be specified with a given where clause (without the
   * WHERE keyword).
   * 
   * @param tableName the not null name of the table where to delete the
   *          records.
   * @param whereClause the where clause of the DELETE statement without the
   *          WHERE keyword.
   * @return an object representing the source for a database table delete.
   */
  public DbDeleteDataSource getDeleteDataSource(String tableName,
      String whereClause)
  {
    DbDeleteDataSource ds = new DbDeleteDataSource(dataSource, tableName,
        whereClause);
    ds.setSqlTypeHandlerRegistry(sqlTypeHandlerRegistry);
    return ds;
  }


  /**
   * Gets a datasource for deleting all records of a table.
   * 
   * @param tableName the not null name of the table where to delete the
   *          records.
   * @return an object representing the source for a database table delete.
   */
  public DbDeleteDataSource getDeleteDataSource(String tableName)
  {
    return getDeleteDataSource(tableName, null);
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
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to insert the record.
   * @return an object representing the source for a (or multiple) database
   *         insert.
   */
  public static DbInsertDataSource getInsertDataSource(DataSource dataSource,
      String tableName)
  {
    return new Database(dataSource).getInsertDataSource(tableName);
  }


  /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to update the record.
   * @param whereClause the where clause of the UPDATE statement without the
   *          WHERE keyword.
   * @return an object representing the source for a (or multiple) database
   *         update.
   */
  public static DbUpdateDataSource getUpdateDataSource(DataSource dataSource,
      String tableName, String whereClause)
  {
    return new Database(dataSource).getUpdateDataSource(tableName, whereClause);
  }


  /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to update the record.
   * @return an object representing the source for a (or multiple) database
   *         update.
   */
  public static DbUpdateDataSource getUpdateDataSource(DataSource dataSource,
      String tableName)
  {
    return new Database(dataSource).getUpdateDataSource(tableName);
  }


  /**
   * Gets a datasource for deleting a set of records of a table. Which records
   * should be deleted can be specified with a given where clause (without the
   * WHERE keyword).
   * 
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to delete the
   *          records.
   * @param whereClause the where clause of the DELETE statement without the
   *          WHERE keyword.
   * @return an object representing the source for a database table delete.
   */
  public static DbDeleteDataSource getDeleteDataSource(DataSource dataSource,
      String tableName, String whereClause)
  {
    return new Database(dataSource).getDeleteDataSource(tableName, whereClause);
  }


  /**
   * Gets a datasource for deleting all records of a table.
   * 
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to delete the
   *          records.
   * @return an object representing the source for a database table delete.
   */
  public static DbDeleteDataSource getDeleteDataSource(DataSource dataSource,
      String tableName)
  {
    return new Database(dataSource).getDeleteDataSource(tableName);
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
  public static <T> BeanMapping<T> getDefaultBeanMapping(
      Class<? extends T> beanClass)
  {
    return getBeanMapping(DbDefaults.INSTANCE.getBeanMappingStrategy(),
        beanClass);
  }


  /**
   * @param mappingStrategy a custom strategy how to map SELECT statement
   *          results to the fields of the given bean.
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement.
   * @return an object describing where to store a SELECT statement result.
   */
  public static <T> BeanMapping<T> getBeanMapping(
      BeanMappingStrategy mappingStrategy, Class<? extends T> beanClass)
  {
    BeanMapping<T> mapping = new BeanMapping<T>(beanClass);
    mapping.setMappingStrategy(mappingStrategy);
    return mapping;
  }

}
