package org.jaqlib;

import javax.sql.DataSource;

import org.jaqlib.db.BeanConventionMappingStrategy;
import org.jaqlib.db.ComplexDbSelectResult;
import org.jaqlib.db.DbSelect;
import org.jaqlib.db.MappingStrategy;
import org.jaqlib.db.SingleDbSelectResult;

/**
 * Helper class that builds objects for executing queries against databases.
 * This class only provides static helper methods and is not intended to be
 * instantiated.
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
   * This method can be used if only one column should be selected.
   * 
   * @param <T> the type of the column.
   * @param columnIndex the index of the column (starting with 1).
   * @return an object defining the result of a SELECT statement with only one
   *         column as result.
   */
  public static <T> SingleDbSelectResult<T> getSingleResult(int columnIndex)
  {
    return new SingleDbSelectResult<T>(columnIndex);
  }


  /**
   * This method can be used if only one column should be selected.
   * 
   * @param <T> the type of the column.
   * @param columnName the name of the column
   * @return an object defining the result of a SELECT statement with only one
   *         column as result.
   */
  public static <T> SingleDbSelectResult<T> getSingleResult(String columnName)
  {
    return new SingleDbSelectResult<T>(columnName);
  }


  /**
   * Creates a {@link ComplexDbSelectResult} instance by using the given bean
   * properties of the given class. Bean properties must have a valid get and
   * set method in order to be in the returned {@link ComplexDbSelectResult}.
   * 
   * @param <T> the type of the result bean.
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statements. Additionally this class is also used to
   *          retrieve the bean properties for storing the result of the SELECT
   *          statement.
   * @return an object describing a SELECT result.
   */
  public static <T> ComplexDbSelectResult<T> getComplexResult(Class<T> beanClass)
  {
    return getComplexResult(new BeanConventionMappingStrategy(beanClass),
        beanClass);
  }


  /**
   * @param mappingStrategy a user-defined strategy how to map SELECT results to
   *          the given bean.
   * @param beanClass the class that should be used to hold the result of the
   *          SELECT statements.
   * @return an object describing a SELECT result.
   */
  public static <T> ComplexDbSelectResult<T> getComplexResult(
      MappingStrategy mappingStrategy, Class<T> beanClass)
  {
    ComplexDbSelectResult<T> result = new ComplexDbSelectResult<T>(beanClass);
    mappingStrategy.execute(result);
    return result;
  }

}
