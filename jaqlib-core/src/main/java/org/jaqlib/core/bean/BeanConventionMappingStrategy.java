package org.jaqlib.core.bean;

import org.jaqlib.util.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of the {@link BeanMappingStrategy} interface that tries to
 * retrieve the bean properties of a given class. These properties are added to
 * a given {@link BeanMapping} object if they have appropriate get and set
 * methods (only regarding Java bean naming convention).
 * 
 * @author Werner Fragner
 */
public class BeanConventionMappingStrategy implements BeanMappingStrategy
{

  private final Logger logger = LogUtil.getLogger(this.getClass());


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
        FieldMapping<?> mapping = getMapping(beanClass, descriptor.getName(),
            descriptor.getPropertyType());
        if (mapping != null)
        {
          mappings.add(mapping);
        }
      }
    }
    return mappings;
  }


  @SuppressWarnings("unchecked")
  private FieldMapping<?> getMapping(Class<?> beanClass, String fieldName,
      Class<?> fieldType)
  {
    if (ReflectionUtil.isPrimitiveType(fieldType))
    {
      return new FieldMapping<>(fieldName, (Class<Object>) fieldType);
    }
    else if (ReflectionUtil.isCollection(fieldType))
    {
      Field field = ReflectionUtil.getField(beanClass, fieldName);
      if (ReflectionUtil.isGeneric(field))
      {
        Class<?> elementType = ReflectionUtil.getCollectionElementClass(field);
        return new CollectionFieldMapping(fieldName,
            (Class<Collection<?>>) fieldType, elementType);
      }
      else
      {
        logger
            .fine("Ignoring field '"
                + beanClass
                + "."
                + fieldName
                + "' because it is a non-generic collection. Only generic collections are supported by this class.");
        return null;
      }
    }
    else
    {
      return new BeanFieldMapping<>(fieldName, (Class<Object>) fieldType);
    }
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
