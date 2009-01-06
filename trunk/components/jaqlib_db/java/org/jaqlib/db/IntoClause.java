package org.jaqlib.db;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class IntoClause<T>
{

  private final T bean;


  public IntoClause(T bean)
  {
    this.bean = Assert.notNull(bean);
  }


  public Using<T> into(DbInsertDataSource dataSource)
  {
    return new Using<T>(bean, dataSource);
  }

}