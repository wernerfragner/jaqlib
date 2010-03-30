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
   * @return a using clause for defining the db column to object field mapping.
   */
  public Using<T> in(DbUpdateDataSource ds)
  {
    return new Using<T>(bean, ds);
  }


  /**
   * Defines the table that should be updated.
   * 
   * @param ds the data source holding the database connection.
   * @param tableName the name of the table that should be updated.
   * @param whereClause the where clause that should be applied to the UPDATE
   *          statement.
   * @return a using clause for defining the db column to object field mapping.
   */
  public Using<T> in(DataSource dataSource, String tableName, String whereClause)
  {
    return in(new DbUpdateDataSource(dataSource, tableName, whereClause));
  }

}
