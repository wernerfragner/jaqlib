package org.jaqlib.db;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class InClause<T>
{

  private final T bean;


  public InClause(T bean)
  {
    this.bean = Assert.notNull(bean);
  }


  public Using<T> in(DbUpdateDataSource ds)
  {
    return new Using<T>(bean, ds);
  }

}
