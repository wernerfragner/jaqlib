package org.jaqlib.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaqlib.db.java.typehandler.DefaultJavaTypeHandlerRegistry;
import org.jaqlib.db.java.typehandler.JavaTypeHandler;
import org.jaqlib.db.java.typehandler.JavaTypeHandlerRegistry;
import org.jaqlib.util.Assert;
import org.jaqlib.util.ReflectionUtil;

/**
 * Defines a mapping between several database columns to the fields of a Java
 * bean. This class also allows nested Java beans by accepting
 * {@link AbstractMapping} in the {@link #addResult(AbstractMapping)} method.
 * 
 * @author Werner Fragner
 * @param <T> the Java bean type of the mapping.
 */
public class BeanMapping<T> extends AbstractMapping<T> implements
    Iterable<AbstractMapping<?>>
{

  private final List<AbstractMapping<?>> mappings = new ArrayList<AbstractMapping<?>>();
  private final Class<T> beanClass;
  private BeanFactory beanFactory = DefaultBeanFactory.INSTANCE;
  private JavaTypeHandlerRegistry javaTypeHandlerRegistry = new DefaultJavaTypeHandlerRegistry();


  /**
   * @param beanClass a not null class of the bean this mapping belongs to.
   */
  public BeanMapping(Class<T> beanClass)
  {
    this.beanClass = Assert.notNull(beanClass);
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
   * @param registry a user-defined java type handler registry.
   */
  public void setJavaTypeHandlerRegistry(JavaTypeHandlerRegistry registry)
  {
    this.javaTypeHandlerRegistry = registry;
  }


  /**
   * @param result a not null mapping (primitive or a bean mapping).
   */
  public void addResult(AbstractMapping<?> result)
  {
    Assert.notNull(result);
    mappings.add(result);
  }


  /**
   * {@inheritDoc}
   */
  public Iterator<AbstractMapping<?>> iterator()
  {
    return mappings.iterator();
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


  private Object applyBeanFieldTypeHandler(String fieldName, Object value)
  {
    Class<?> fieldType = ReflectionUtil.getFieldType(beanClass, fieldName);
    return applyJavaTypeHandler(fieldType, value);
  }


  private void setValue(T bean, String fieldName, Object value)
  {
    value = applyBeanFieldTypeHandler(fieldName, value);
    ReflectionUtil.setFieldValue(bean, fieldName, value);
  }


  public Object applyJavaTypeHandler(Class<?> fieldType, Object value)
  {
    return getJavaTypeHandler(fieldType).getObject(value);
  }


  private JavaTypeHandler getJavaTypeHandler(Class<?> fieldType)
  {
    return javaTypeHandlerRegistry.getTypeHandler(fieldType);
  }

}
