package org.jaqlib;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.Defaults;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.xml.XmlFromClause;
import org.jaqlib.xml.XmlQuery;


public class XmlQueryBuilder extends AbstractQueryBuilder
{

  public <T> XmlFromClause<T> select(Class<T> beanClass)
  {
    BeanMapping<T> beanMapping = getDefaultBeanMapping(beanClass);
    return select(beanMapping);
  }


  public <T> XmlFromClause<T> select(BeanMapping<T> beanMapping)
  {
    return this.<T> createQuery().createFromClause(beanMapping);
  }


  private <T> XmlQuery<T> createQuery()
  {
    return new XmlQuery<T>(getMethodCallRecorder());
  }


  public <T> BeanMapping<T> getDefaultBeanMapping(Class<T> beanClass)
  {
    BeanMapping<T> beanMapping = new BeanMapping<T>(beanClass);
    beanMapping.setMappingStrategy(Defaults.getMappingStrategy());
    return beanMapping;
  }

}
