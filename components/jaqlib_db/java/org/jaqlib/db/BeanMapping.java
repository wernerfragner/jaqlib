package org.jaqlib.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaqlib.util.Assert;
import org.jaqlib.util.ExceptionUtil;
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


  /**
   * @param beanClass a not null class of the bean this mapping belongs to.
   */
  public BeanMapping(Class<T> beanClass)
  {
    this.beanClass = Assert.notNull(beanClass);
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
    try
    {
      return beanClass.newInstance();
    }
    catch (InstantiationException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
    catch (IllegalAccessException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
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
        final String fieldName = mapping.getFieldName();
        value = applyBeanFieldTypeHandler(rs, fieldName, value);
        setValue(bean, fieldName, value);
      }
    }
    return bean;
  }


  private Object applyBeanFieldTypeHandler(DbResultSet rs, String fieldName,
      Object value)
  {
    Class<?> fieldType = ReflectionUtil.getFieldType(beanClass, fieldName);
    return rs.applyBeanFieldTypeHandler(fieldType, value);
  }


  private void setValue(T bean, String fieldName, Object value)
  {
    ReflectionUtil.setFieldValue(bean, fieldName, value);
  }

}
