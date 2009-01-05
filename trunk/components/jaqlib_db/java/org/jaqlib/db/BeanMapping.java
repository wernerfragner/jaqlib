package org.jaqlib.db;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.jaqlib.db.java.typehandler.JavaTypeHandler;
import org.jaqlib.db.java.typehandler.JavaTypeHandlerRegistry;
import org.jaqlib.util.Assert;
import org.jaqlib.util.ReflectionUtil;

/**
 * Defines a mapping between several database columns to the fields of a Java
 * bean. The strategy how to do this mapping can be defined by setting a custom
 * {@link MappingStrategy}. By default the {@link BeanConventionMappingStrategy}
 * is used.
 * 
 * @author Werner Fragner
 * @param <T> the Java bean type of the mapping.
 */
public class BeanMapping<T> extends AbstractMapping<T> implements
    Iterable<AbstractMapping<?>>
{

  // state

  private final Class<? extends T> beanClass;
  private List<AbstractMapping<?>> mappings;

  // infrastructure

  private BeanFactory beanFactory = Defaults.getBeanFactory();
  private JavaTypeHandlerRegistry javaTypeHandlerRegistry = Defaults
      .getJavaTypeHandlerRegistry();
  private MappingStrategy mappingStrategy = Defaults.getMappingStrategy();


  /**
   * @param beanClass a not null class of the bean this mapping belongs to.
   */
  public BeanMapping(Class<? extends T> beanClass)
  {
    this.beanClass = Assert.notNull(beanClass);
  }


  /**
   * Sets a custom strategy how to retrieve the mapping between database columns
   * and Java bean fields.
   * 
   * @param strategy a not null custom strategy.
   */
  public void setMappingStrategy(MappingStrategy strategy)
  {
    this.mappingStrategy = Assert.notNull(strategy);
  }


  /**
   * Sets a custom bean factory for creating bean instances.
   * 
   * @param beanFactory a not null bean factory.
   */
  public void setBeanFactory(BeanFactory beanFactory)
  {
    this.beanFactory = Assert.notNull(beanFactory);
  }


  /**
   * Registers a custom java type handler with a given java type.
   * 
   * @param fieldType a not null java type.
   * @param typeHandler a not null custom java type handler.
   */
  public void registerJavaTypeHandler(Class<?> fieldType,
      JavaTypeHandler typeHandler)
  {
    javaTypeHandlerRegistry.registerTypeHandler(fieldType, typeHandler);
  }


  /**
   * Changes the java type handler registry to a custom implementation. By
   * default no type handlers are available.
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
  public Iterator<AbstractMapping<?>> iterator()
  {
    return getMappings().iterator();
  }


  private List<AbstractMapping<?>> getMappings()
  {
    if (mappings == null)
    {
      mappings = mappingStrategy.getMappings(beanClass);
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


  @Override
  public T getValue(DbResultSet rs) throws SQLException
  {
    T bean = newBeanInstance();
    for (AbstractMapping<?> mapping : this)
    {
      Object value = mapping.getValue(rs);
      if (value != DbResultSet.NO_RESULT)
      {
        setValue(bean, mapping.getFieldName(), value);
      }
    }
    return bean;
  }


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


  public AbstractMapping<?> removeChildMapping(String fieldName)
  {
    AbstractMapping<?> mapping = getChildMapping(fieldName);
    if (mapping != null)
    {
      getMappings().remove(mapping);
    }
    return mapping;
  }


  public ColumnMapping<?> getChildColumn(String fieldName)
  {
    return (ColumnMapping<?>) getChildMapping(fieldName);
  }


  public AbstractMapping<?> getChildMapping(String fieldName)
  {
    for (AbstractMapping<?> mapping : getMappings())
    {
      if (fieldName.equals(mapping.getFieldName()))
      {
        return mapping;
      }
    }
    return null;
  }


  @Override
  public String getLogString()
  {
    return ReflectionUtil.getPlainClassName(beanClass);
  }


  @Override
  public void appendColumn(StringBuilder columns, StringBuilder values)
  {
    boolean first = true;
    for (AbstractMapping<?> mapping : getMappings())
    {
      if (!first)
      {
        columns.append(", ");
        values.append(", ");
      }
      else
      {
        first = false;
      }
      mapping.appendColumn(columns, values);
    }
  }

}
