package org.jaqlib;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.xml.XmlFromClause;
import org.jaqlib.xml.XmlQuery;

import com.sun.org.apache.xml.internal.serialize.OutputFormat.Defaults;


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


  public BeanMapping<T> getDefaultBeanMapping(Class<T> beanClass)
  {
    BeanMapping<T> beanMapping = new BeanMapping<T>(beanClass);
    beanMapping.setMappingStrategy(Defaults.getMappingStrategy());
    return beanMapping;
  }

}
