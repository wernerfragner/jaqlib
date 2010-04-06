package org.jaqlib.db;

import javax.sql.DataSource;

import org.jaqlib.util.Assert;

/**
 * Defines which table should be updated.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the type of the bean that should be updated.
 */
public class InClause<T>
{

  private final T bean;


  public InClause(T bean)
  {
    this.bean = Assert.notNull(bean);
  }


  /**
   * Defines the table that should be updated.
   * 
   * @param ds holds information about the table that should be updated.
   * @return a using clause for defining the DB column to object field mapping.
   */
  public UpdateUsing<T> in(DbUpdateDataSource ds)
  {
    return new UpdateUsing<T>(bean, ds);
  }


  /**
   * Defines the table that should be updated.
   * 
   * @param dataSource the data source holding the database connection.
   * @param tableName the name of the table that should be updated.
   * @return a using clause for defining the DB column to object field mapping.
   */
  public UpdateUsing<T> in(DataSource dataSource, String tableName)
  {
    return in(new DbUpdateDataSource(dataSource, tableName, null));
  }

}
