package org.jaqlib;

import javax.sql.DataSource;

import org.jaqlib.query.db.BeanConventionMappingRetrievalStrategy;
import org.jaqlib.query.db.BeanDbSelectResult;
import org.jaqlib.query.db.DbSelectDataSource;
import org.jaqlib.query.db.MappingRetrievalStrategy;
import org.jaqlib.util.Assert;

/**
 * <p>
 * Helper class that builds objects for executing queries against databases.
 * This class provides static helper methods but can also be instantiated to
 * make the creation of {@link DbSelectDataSource} and
 * {@link BeanDbSelectResult} objects more comfortable. <br>
 * </p>
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * @author Werner Fragner
 */
public class Database
{

  private static MappingRetrievalStrategy defaultMappingRetrievalStrategy = new BeanConventionMappingRetrievalStrategy();

  private final DataSource dataSource;
  private MappingRetrievalStrategy mappingRetrievalStrategy;


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


  private MappingRetrievalStrategy getMappingRetrievalStrategy()
  {
    if (this.mappingRetrievalStrategy == null)
    {
      return defaultMappingRetrievalStrategy;
    }
    else
    {
      return this.mappingRetrievalStrategy;
    }
  }


  /**
   * 
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement.
   * @return an object describing a SELECT statement result.
   */
  public <T> BeanDbSelectResult<T> getBeanResult(Class<T> beanClass)
  {
    return getBeanResult(getMappingRetrievalStrategy(), beanClass);
  }


  /**
   * @param sql a not null SELECT statement.
   * @return an object representing the source for a database query.
   */
  public DbSelectDataSource getSelectDataSource(String sql)
  {
    return new DbSelectDataSource(dataSource, sql);
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


  private static <T> MappingRetrievalStrategy getDefaultMappingRetrievalStrategy(
      Class<T> beanClass)
  {
    return defaultMappingRetrievalStrategy;
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
   * Creates a {@link BeanDbSelectResult} instance by using the bean properties
   * of the given class. Bean properties must have a valid get and set method in
   * order to be in the returned {@link BeanDbSelectResult}.
   * 
   * @param <T> the type of the result bean.
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement. Additionally this class is used to retrieve the
   *          bean properties for storing the result of the SELECT statement.
   * @return an object describing a SELECT statement result.
   */
  public static <T> BeanDbSelectResult<T> getDefaultBeanResult(
      Class<T> beanClass)
  {
    final MappingRetrievalStrategy strategy = getDefaultMappingRetrievalStrategy(beanClass);
    return getBeanResult(strategy, beanClass);
  }


  /**
   * @param mappingStrategy a user-defined strategy how to map SELECT statement
   *          results to the fields of the given bean.
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement.
   * @return an object describing a SELECT statement result.
   */
  public static <T> BeanDbSelectResult<T> getBeanResult(
      MappingRetrievalStrategy mappingStrategy, Class<T> beanClass)
  {
    BeanDbSelectResult<T> result = new BeanDbSelectResult<T>(beanClass);
    mappingStrategy.addMappings(beanClass, result);
    return result;
  }


}
