package org.jaqlib;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.xml.XmlFromClause;
import org.jaqlib.xml.XmlQuery;


public class XmlQueryBuilder extends AbstractQueryBuilder
{

  /**
   * Contains the application wide default values for the XML query builder.
   */
  public static final XmlDefaults DEFAULTS = XmlDefaults.INSTANCE;


  public <T> XmlFromClause<T> select(Class<T> beanClass)
  {
    BeanMapping<T> beanMapping = getDefaultBeanMapping(beanClass);
    return select(beanMapping);
  }


  public <T> XmlFromClause<T> select(BeanMapping<T> beanMapping)
  {
    return this.<T> createQuery(beanMapping).createFromClause();
  }


  private <T> XmlQuery<T> createQuery(BeanMapping<T> beanMapping)
  {
    return new XmlQuery<T>(getMethodCallRecorder(), beanMapping);
  }


  public <T> BeanMapping<T> getDefaultBeanMapping(Class<T> beanClass)
  {
    BeanMapping<T> beanMapping = new BeanMapping<T>(beanClass);
    beanMapping.setMappingStrategy(XmlDefaults.INSTANCE.getMappingStrategy());
    return beanMapping;
  }

}
