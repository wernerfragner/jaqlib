package org.jaqlib.core.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;

import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;
import org.jaqlib.util.ExceptionUtil;

/**
 * Implementation of the {@link MappingStrategy} interface that tries to
 * retrieve the bean properties of a given class. These properties are added to
 * a given {@link BeanMapping} object if they have appropriate get and set
 * methods (only regarding Java bean naming convention).
 * 
 * @author Werner Fragner
 */
public class BeanConventionMappingStrategy implements MappingStrategy
{

  /**
   * {@inheritDoc}
   */
  public List<FieldMapping<?>> getMappings(Class<?> beanClass)
  {
    Assert.notNull(beanClass);

    List<FieldMapping<?>> mappings = CollectionUtil.newDefaultList();
    BeanInfo beanInfo = getBeanInfo(beanClass);
    for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors())
    {
      if (shouldAddBeanProperty(descriptor))
      {
        mappings.add(getPrimitiveResult(descriptor.getName(), descriptor
            .getPropertyType()));
      }
    }
    return mappings;
  }


  private FieldMapping<?> getPrimitiveResult(String fieldName,
      Class<?> fieldType)
  {
    return new FieldMapping<Object>(fieldName, fieldType);
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
