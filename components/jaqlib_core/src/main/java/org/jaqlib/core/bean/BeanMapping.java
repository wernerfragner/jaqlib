package org.jaqlib.core.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaqlib.core.Defaults;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.util.Assert;
import org.jaqlib.util.ReflectionUtil;


/**
 * Defines a mapping between source data and the fields of a Java bean. The
 * mappings for the single fields are defined using {@link FieldMapping} and
 * {@link CollectionFieldMapping} objects. The strategy how to do this mapping
 * can be defined by setting a custom {@link BeanMappingStrategy}. By default
 * the {@link BeanConventionMappingStrategy} is used.
 * 
 * @author Werner Fragner
 * @param <T> the Java bean type of the mapping.
 */
public class BeanMapping<T> extends AbstractMapping<T> implements
    Iterable<FieldMapping<?>>
{

  // state

  private final Class<? extends T> beanClass;
  private List<FieldMapping<?>> mappings;

  // infrastructure

  private BeanFactory beanFactory = Defaults.getBeanFactory();
  private JavaTypeHandlerRegistry javaTypeHandlerRegistry = Defaults
      .getJavaTypeHandlerRegistry();
  private BeanMappingStrategy mappingStrategy = Defaults
      .getBeanMappingStrategy();


  /**
   * Creates a new bean mapping for the given bean class.
   * 
   * @param beanClass a not null class of the bean this mapping belongs to.
   */
  public BeanMapping(Class<? extends T> beanClass)
  {
    this.beanClass = Assert.notNull(beanClass);
  }


  /**
   * Sets a custom strategy how to retrieve the mapping between data source
   * fields and Java bean fields.
   * 
   * @param strategy a not null custom strategy.
   */
  public void setMappingStrategy(BeanMappingStrategy strategy)
  {
    this.mappingStrategy = Assert.notNull(strategy);
  }


  /**
   * Sets a custom bean factory for creating bean instances.
   * 
   * @param beanFactory a not null bean factory.
   */
  public void setFactory(BeanFactory beanFactory)
  {
    this.beanFactory = Assert.notNull(beanFactory);
  }


  /**
   * Registers a custom java type handler. By default no type handlers are
   * available. The source and target type must match without conversion.
   * 
   * @param typeHandler a not null custom java type handler.
   */
  public void registerJavaTypeHandler(JavaTypeHandler typeHandler)
  {
    javaTypeHandlerRegistry.registerTypeHandler(typeHandler);
  }


  /**
   * Changes the java type handler registry to a custom implementation. By
   * default no type handlers are available. The source and target type must
   * match without conversion.
   * 
   * @param registry a custom java type handler registry.
   */
  public void setJavaTypeHandlerRegistry(JavaTypeHandlerRegistry registry)
  {
    this.javaTypeHandlerRegistry = Assert.notNull(registry);
  }


  /**
   * {@inheritDoc}
   */
  public Iterator<FieldMapping<?>> iterator()
  {
    return getMappings().iterator();
  }


  private List<FieldMapping<?>> getMappings()
  {
    if (mappings == null)
    {
      mappings = mappingStrategy.getMappings(beanClass);
      if (mappings == null)
      {
        mappings = new ArrayList<>();
      }
    }
    return mappings;
  }


  /**
   * @return a new Java bean instance for this mapping.
   * @throws RuntimeException if the bean instance cannot be created.
   */
  private T newBeanInstance()
  {
    return beanFactory.newInstance(beanClass);
  }


  /**
   * This method is for internal use only. It should <b>NOT</b> be called by the
   * Jaqlib user.
   */
  @Override
  public T getValue(DsResultSet rs)
  {
    if (isPrimitive())
    {
      return getPrimitive(rs);
    }
    else
    {
      return getBean(rs);
    }
  }


  private T getBean(DsResultSet rs)
  {
    T bean = newBeanInstance();
    for (FieldMapping<?> mapping : this)
    {
      Object value = mapping.getValue(rs);
      if (value != DsResultSet.NO_RESULT)
      {
        setValue(bean, mapping.getTargetName(), value);
      }
    }
    return bean;
  }


  private T getPrimitive(DsResultSet rs)
  {
    FieldMapping<T> mapping = new FieldMapping<>();
    mapping.setFieldType(this.beanClass);
    mapping.setTypeHandler(getJavaTypeHandler(this.beanClass));

    return mapping.getAnonymousValue(rs);
  }


  private boolean isPrimitive()
  {
    return ReflectionUtil.isPrimitiveType(this.beanClass);
  }


  /**
   * This method is for internal use only. It should <b>NOT</b> be called by the
   * Jaqlib user.
   */
  public Object applyJavaTypeHandler(String fieldName, Object value)
  {
    Class<?> fieldType = ReflectionUtil.getFieldType(beanClass, fieldName);
    return applyJavaTypeHandler(fieldType, value);
  }


  private Object applyJavaTypeHandler(Class<?> fieldType, Object value)
  {
    return getJavaTypeHandler(fieldType).convert(value);
  }


  private void setValue(T bean, String fieldName, Object value)
  {
    value = applyJavaTypeHandler(fieldName, value);
    ReflectionUtil.setFieldValue(bean, fieldName, value);
  }


  private JavaTypeHandler getJavaTypeHandler(Class<?> fieldType)
  {
    return javaTypeHandlerRegistry.getTypeHandler(fieldType);
  }


  /**
   * Adds the given field to this bean mapping. The given fieldName is used as
   * source and target name.
   * 
   * @param fieldName the Java bean field name.
   * @param fieldType the type of the Java bean field.
   * @return the added field. This object can be used to specify further
   *         properties on the field mapping.
   */
  public <FT> FieldMapping<FT> addField(String fieldName, Class<FT> fieldType)
  {
    FieldMapping<FT> field = new FieldMapping<>(fieldName, fieldType);
    addField(field);
    return field;
  }


  /**
   * Adds the given field to this bean mapping. If the given field is null then
   * no action is performed.
   * 
   * @param field the field to add.
   * @return the given field.
   */
  public <FT> FieldMapping<FT> addField(FieldMapping<FT> field)
  {
    if (field != null)
    {
      this.mappings.add(field);
    }
    return field;
  }


  /**
   * Removes the given field from this bean mapping. If no matching field
   * mapping has been found then no action is performed.
   * 
   * @param fieldName the field mapping to remove.
   * @return the removed field mapping; null if no matching field mapping has
   *         been found.
   */
  public FieldMapping<?> removeField(String fieldName)
  {
    FieldMapping<?> mapping = getField(fieldName);
    if (mapping != null)
    {
      getMappings().remove(mapping);
    }
    return mapping;
  }


  /**
   * Returns true if this bean mapping contains a mapping for the given field.
   * 
   * @param fieldName the name of the field; must not be null.
   * @return see description.
   */
  public boolean hasField(String fieldName)
  {
    return (getField(fieldName) != null);
  }


  /**
   * Gets the field mapping for the given field of this bean. Returns null if no
   * matching field has been found.
   * 
   * @param fieldName the name of the field; must not be null.
   * @return see description.
   */
  public FieldMapping<?> getField(String fieldName)
  {
    for (FieldMapping<?> mapping : getMappings())
    {
      if (fieldName.equals(mapping.getTargetName()))
      {
        return mapping;
      }
    }
    return null;
  }


  /**
   * Checks if the given field is a collection field. Returns false if it is no
   * collection field or if the field does not exist in this bean mapping.
   * 
   * @param fieldName the name of the field.
   * @return see description.
   */
  public boolean isCollectionField(String fieldName)
  {
    FieldMapping<?> field = getField(fieldName);
    return (field instanceof CollectionFieldMapping);
  }


  /**
   * Tries to get the given collection field mapping. Returns null if no
   * matching field has been found.
   * 
   * @param fieldName the name of the field.
   * @return see description.
   * @throws IllegalArgumentException if the given field was found but is no
   *           collection field.
   */
  public CollectionFieldMapping getCollectionField(String fieldName)
  {
    FieldMapping<?> field = getField(fieldName);
    if (field == null)
    {
      return null;
    }
    else if (field instanceof CollectionFieldMapping)
    {
      return (CollectionFieldMapping) field;
    }
    else
    {
      throw new IllegalArgumentException("The given field '" + fieldName
          + "' has no collection type. It is of type '" + field.getFieldType()
          + "'.");
    }
  }


  /**
   * Checks if the given field is a bean field. Returns false if it is no bean
   * field or if the field does not exist in this bean mapping.
   * 
   * @param fieldName the name of the field.
   * @return see description.
   */
  public boolean isBeanField(String fieldName)
  {
    FieldMapping<?> field = getField(fieldName);
    return (field instanceof BeanFieldMapping);
  }


  /**
   * Tries to get the given bean field mapping. Returns null if no matching
   * field has been found.
   * 
   * @param fieldName the name of the field.
   * @return see description.
   * @throws IllegalArgumentException if the given field was found but is no
   *           bean field.
   */
  public BeanFieldMapping<?> getBeanField(String fieldName)
  {
    FieldMapping<?> field = getField(fieldName);
    if (field == null)
    {
      return null;
    }
    else if (field instanceof BeanFieldMapping)
    {
      return (BeanFieldMapping<?>) field;
    }
    else
    {
      throw new IllegalArgumentException("The given field '" + fieldName
          + "' has no bean type. It is of type '" + field.getFieldType() + "'.");
    }
  }


  /**
   * Removes all field mappings. After calling this method new field mapping can
   * only be added by using {@link #addField(FieldMapping)} .
   */
  public void removeAllFields()
  {
    this.mappings = new ArrayList<>();
  }


  /**
   * Gets a copy of all field mappings of this bean mapping. Changes to the
   * returned list (e.g. add, remove) do <b>NOT</b> affect this object.
   * 
   * @return see description.
   */
  public List<FieldMapping<?>> getFieldMappings()
  {
    return new ArrayList<>(this.getMappings());
  }


  @Override
  public String getLogString()
  {
    return ReflectionUtil.getPlainClassName(beanClass);
  }


  /**
   * Creates a {@link BeanMapping} instance by using the bean properties of the
   * given class. Bean properties must have a valid get and set method in order
   * to be in the returned {@link BeanMapping}.
   * 
   * @param <T> the type of the result bean.
   * @param beanClass the class that should be used to hold the result of the
   *          query. Additionally this class is used to retrieve the bean
   *          properties for storing the result of the query.
   * @return an object describing how query results are mapped to Java beans.
   */
  public static <T> BeanMapping<T> build(Class<? extends T> beanClass)
  {
    return build(Defaults.getBeanMappingStrategy(), beanClass);
  }


  /**
   * @param mappingStrategy a custom strategy how to map query results to the
   *          fields of the given bean.
   * @param beanClass the class that should be used to hold the result of the
   *          query.
   * @return an object describing how query results are mapped to Java beans.
   */
  public static <T> BeanMapping<T> build(BeanMappingStrategy mappingStrategy,
      Class<? extends T> beanClass)
  {
    BeanMapping<T> beanMapping = new BeanMapping<>(beanClass);
    beanMapping.setMappingStrategy(mappingStrategy);
    return beanMapping;
  }

}
