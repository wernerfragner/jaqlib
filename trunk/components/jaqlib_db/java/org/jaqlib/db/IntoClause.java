package org.jaqlib.db;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class IntoClause<T>
{

  private final T element;
  private final BeanMapping<T> beanMapping;


  public IntoClause(T element, BeanMapping<T> beanMapping)
  {
    this.element = Assert.notNull(element);
    this.beanMapping = Assert.notNull(beanMapping);
  }


  public void into(DbInsertDataSource dataSource)
  {
    dataSource.execute(element, beanMapping);
  }

}
