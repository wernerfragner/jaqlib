package org.jaqlib.db;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.jaqlib.Db;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public class BeanConventionMappingStrategy implements MappingStrategy
{

  private final Class<?> beanClass;


  public BeanConventionMappingStrategy(Class<?> beanClass)
  {
    this.beanClass = Assert.notNull(beanClass);
  }


  public <T> void execute(ComplexDbSelectResult<T> result)
  {
    Assert.notNull(result);

    BeanInfo beanInfo = getBeanInfo();
    for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors())
    {
      if (shouldAddBeanProperty(descriptor))
      {
        result.addResult(Db.getSingleResult(descriptor.getName()));
      }
    }
  }


  protected boolean shouldAddBeanProperty(PropertyDescriptor descriptor)
  {
    return hasReadAndWriteMethods(descriptor);
  }


  private boolean hasReadAndWriteMethods(PropertyDescriptor descriptor)
  {
    return descriptor.getReadMethod() != null
        && descriptor.getWriteMethod() != null;
  }


  private BeanInfo getBeanInfo()
  {
    try
    {
      return Introspector.getBeanInfo(beanClass);
    }
    catch (IntrospectionException e)
    {
      throw new RuntimeException(e);
    }
  }

}
