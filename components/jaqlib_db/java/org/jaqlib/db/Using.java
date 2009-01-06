package org.jaqlib.db;

import org.jaqlib.Database;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class Using<T>
{

  private final T bean;
  private final AbstractDbDmlDataSource dataSource;


  public Using(T bean, AbstractDbDmlDataSource dataSource)
  {
    this.bean = Assert.notNull(bean);
    this.dataSource = Assert.notNull(dataSource);
  }


  public void using(BeanMapping<T> beanMapping)
  {
    dataSource.execute(bean, beanMapping);
  }


  public void usingDefault()
  {
    using(getDefaultBeanMapping());
  }


  private BeanMapping<T> getDefaultBeanMapping()
  {
    @SuppressWarnings("unchecked")
    Class<T> beanClass = (Class<T>) bean.getClass();
    return Database.getDefaultBeanMapping(beanClass);
  }

}
