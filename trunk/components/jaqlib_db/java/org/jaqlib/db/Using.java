package org.jaqlib.db;

import org.jaqlib.Database;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class Using<T>
{

  protected final T bean;
  protected final AbstractDbDmlDataSource dataSource;


  public Using(T bean, AbstractDbDmlDataSource dataSource)
  {
    this.bean = Assert.notNull(bean);
    this.dataSource = Assert.notNull(dataSource);
  }


  public int using(BeanMapping<T> beanMapping)
  {
    return dataSource.execute(bean, beanMapping);
  }


  public int usingDefaultMapping()
  {
    return using(getDefaultBeanMapping());
  }


  private BeanMapping<T> getDefaultBeanMapping()
  {
    @SuppressWarnings("unchecked")
    Class<T> beanClass = (Class<T>) bean.getClass();
    return Database.getDefaultBeanMapping(beanClass);
  }

}
