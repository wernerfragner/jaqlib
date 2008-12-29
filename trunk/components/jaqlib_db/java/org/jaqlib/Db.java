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


  public static DbSelect getSelect(DataSource dataSource, String sql)
  {
    return new DbSelect(dataSource, sql);
  }


  public static <T> SingleDbSelectResult<T> getSingleResult(int columnIndex)
  {
    return new SingleDbSelectResult<T>(columnIndex);
  }


  public static <T> SingleDbSelectResult<T> getSingleResult(String columnName)
  {
    return new SingleDbSelectResult<T>(columnName);
  }


  public static <T> ComplexDbSelectResult<T> getComplexResult(Class<T> beanClass)
  {
    return getComplexResult(new BeanConventionMappingStrategy(beanClass));
  }


  public static <T> ComplexDbSelectResult<T> getComplexResult(
      MappingStrategy mappingStrategy)
  {
    ComplexDbSelectResult<T> result = new ComplexDbSelectResult<T>();
    mappingStrategy.execute(result);
    return result;
  }

}
