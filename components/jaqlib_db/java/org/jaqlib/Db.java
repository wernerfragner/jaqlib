package org.jaqlib;

import javax.sql.DataSource;

import org.jaqlib.db.BeanConventionMappingRetrievalStrategy;
import org.jaqlib.db.BeanDbSelectResult;
import org.jaqlib.db.DbSelect;
import org.jaqlib.db.MappingRetrievalStrategy;
import org.jaqlib.db.PrimitiveDbSelectResult;

/**
 * Helper class that builds objects for executing queries against databases.
 * This class only provides static helper methods and is not intended to be
 * instantiated. <br>
 * This class is thread-safe.
 * 
 * @author Werner Fragner
 */
public class Db
{

  private Db()
  {
  }


  /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param sql a not null SELECT statement.
   * @return a object representing the source for a database query.
   */
  public static DbSelect getSelect(DataSource dataSource, String sql)
  {
    return new DbSelect(dataSource, sql);
  }


  /**
   * This method can be used if only one column should be selected into a
   * primitive Java type.
   * 
   * @param <T> the type of the column.
   * @param columnIndex the index of the column (starting with 1).
   * @return an object defining the result of a SELECT statement with only one
   *         column as result.
   */
  public static <T> PrimitiveDbSelectResult<T> getPrimitiveResult(
      int columnIndex)
  {
    return new PrimitiveDbSelectResult<T>(columnIndex);
  }


  /**
   * This method can be used if only one column should be selected into a
   * primitive Java type.
   * 
   * @param <T> the type of the column.
   * @param columnName the name of the column
   * @return an object defining the result of a SELECT statement with only one
   *         column as result.
   */
  public static <T> PrimitiveDbSelectResult<T> getPrimitiveResult(
      String columnName)
  {
    return new PrimitiveDbSelectResult<T>(columnName);
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
