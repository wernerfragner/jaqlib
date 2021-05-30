package org.jaqlib.db;

import org.jaqlib.util.Assert;

import javax.sql.DataSource;

/**
 * The into clause of INSERT statements.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the type of the bean that should be inserted.
 */
public class IntoClause<T>
{

  private final T bean;


  public IntoClause(T bean)
  {
    this.bean = Assert.notNull(bean);
  }


  /**
   * Defines the target for the INSERT statement.
   * 
   * @param dataSource a not null data source for the INSERT statement.
   * @return a using clause for defining the db column to object field mapping.
   */
  public Using<T> into(DbInsertDataSource dataSource)
  {
    return new Using<T>(bean, dataSource);
  }


  /**
   * Defines the target for the INSERT statement.
   * 
   * @param dataSource a not null data source for the INSERT statement.
   * @param tableName the name of the table into which to insert the bean.
   * @return a using clause for defining the db column to object field mapping.
   */
  public Using<T> into(DataSource dataSource, String tableName)
  {
    return into(new DbInsertDataSource(dataSource, tableName));
  }

}
