package org.jaqlib;

import javax.sql.DataSource;

import org.jaqlib.db.BeanConventionMappingRetrievalStrategy;
import org.jaqlib.db.BeanDbSelectResult;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.MappingRetrievalStrategy;
import org.jaqlib.util.Assert;

/**
 * Helper class that builds objects for executing queries against databases.
 * This class provides static helper methods but can also be instantiated to
 * make the creation of {@link DbSelectDataSource} objects more comfortable. <br>
 * This class is thread-safe.
 * 
 * @author Werner Fragner
 */
public class Database
{

  private final DataSource dataSource;


  /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   */
  public Database(DataSource dataSource)
  {
    this.dataSource = Assert.notNull(dataSource);
  }


  /**
   * @param sql a not null SELECT statement.
   * @return a object representing the source for a database query.
   */
  public DbSelectDataSource getSelectDataSource(String sql)
  {
    return new DbSelectDataSource(dataSource, sql);
  }


  /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param sql a not null SELECT statement.
   * @return a object representing the source for a database query.
   */
  public static DbSelectDataSource getSelectDataSource(DataSource dataSource, String sql)
  {
    return new Database(dataSource).getSelectDataSource(sql);
  }


  /**
   * Creates a {@link BeanDbSelectResult} instance by using the given bean
   * properties of the given class. Bean properties must have a valid get and
   * set method in order to be in the returned {@link BeanDbSelectResult}.
   * 
   * @param <T> the type of the result bean.
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statement. Additionally this class is used to retrieve the
   *          bean properties for storing the result of the SELECT statement.
   * @return an object describing a SELECT statement result.
   */
  public static <T> BeanDbSelectResult<T> getBeanResult(Class<T> beanClass)
  {
    final MappingRetrievalStrategy strategy = new BeanConventionMappingRetrievalStrategy(
        beanClass);
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
    mappingStrategy.addMappings(result);
    return result;
  }

}
