package org.jaqlib.query.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaqlib.util.Assert;
import org.jaqlib.util.ExceptionUtil;

/**
 * Defines a mapping between several database columns to the fields of a Java
 * bean. This class also allows nested Java beans by accepting
 * {@link DbSelectResult} in the {@link #addResult(DbSelectResult)} method.
 * 
 * @author Werner Fragner
 * @param <T> the Java bean type of the mapping.
 */
public class BeanDbSelectResult<T> extends DbSelectResult<T> implements
    Iterable<DbSelectResult<?>>
{

  private final List<DbSelectResult<?>> results = new ArrayList<DbSelectResult<?>>();
  private final Class<T> beanClass;


  /**
   * @param beanClass a not null class of the bean this mapping belongs to.
   */
  public BeanDbSelectResult(Class<T> beanClass)
  {
    this.beanClass = Assert.notNull(beanClass);
  }


  /**
   * @param result a not null mapping (primitive or a bean mapping).
   */
  public void addResult(DbSelectResult<?> result)
  {
    Assert.notNull(result);
    results.add(result);
  }


  /**
   * {@inheritDoc}
   */
  public Iterator<DbSelectResult<?>> iterator()
  {
    return results.iterator();
  }


  /**
   * @return a new Java bean instance for this mapping.
   * @throws RuntimeException if the bean instance cannot be created.
   */
  public T newBeanInstance()
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

}
