package org.jaqlib.db;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.jaqlib.util.Assert;
import org.jaqlib.util.ExceptionUtil;

/**
 * Implementation of the {@link MappingRetrievalStrategy} interface that tries
 * to retrieve the bean properties of a given class. These properties are added
 * to a given {@link BeanMapping} object if they have appropriate get and
 * set methods (only regarding Java bean naming convention).
 * 
 * @author Werner Fragner
 */
public class BeanConventionMappingRetrievalStrategy implements
    MappingRetrievalStrategy
{

  /**
   * {@inheritDoc}
   */
  public void addMappings(Class<?> beanClass, BeanMapping<?> result)
  {
    Assert.notNull(beanClass);
    Assert.notNull(result);

    BeanInfo beanInfo = getBeanInfo(beanClass);
    for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors())
    {
      if (shouldAddBeanProperty(descriptor))
      {
        result.addResult(getPrimitiveResult(descriptor.getName()));
      }
    }
  }


  private <T> ColumnMapping<T> getPrimitiveResult(String columnName)
  {
    return new ColumnMapping<T>(columnName);
  }


  /**
   * This method can be overridden in order to adapt the logic which bean
   * properties to add to the mapping result.
   * 
   * @param descriptor a not null bean property descriptor.
   * @return true if the property should be added to the mapping result.
   */
  protected boolean shouldAddBeanProperty(PropertyDescriptor descriptor)
  {
    return hasReadAndWriteMethods(descriptor);
  }


  private boolean hasReadAndWriteMethods(PropertyDescriptor descriptor)
  {
    return descriptor.getReadMethod() != null
        && descriptor.getWriteMethod() != null;
  }


  private BeanInfo getBeanInfo(Class<?> beanClass)
  {
    try
    {
      return Introspector.getBeanInfo(beanClass);
    }
    catch (IntrospectionException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }

}
