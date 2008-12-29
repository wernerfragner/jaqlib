package org.jaqlib.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaqlib.util.Assert;
import org.jaqlib.util.ExceptionUtil;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class ComplexDbSelectResult<T> extends DbSelectResult<T> implements
    Iterable<DbSelectResult<?>>
{

  private final List<DbSelectResult<?>> results = new ArrayList<DbSelectResult<?>>();
  private final Class<T> beanClass;


  public ComplexDbSelectResult(Class<T> beanClass)
  {
    this.beanClass = Assert.notNull(beanClass);
  }


  public void addResult(DbSelectResult<?> result)
  {
    Assert.notNull(result);
    results.add(result);
  }


  public Iterator<DbSelectResult<?>> iterator()
  {
    return results.iterator();
  }


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
