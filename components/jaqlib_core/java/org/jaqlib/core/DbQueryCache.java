package org.jaqlib.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;


/**
 * @author Werner Fragner
 * 
 * @param <T> the element type of the query.
 */
public class DbQueryCache<T>
{

  private final ElementPredicate<T> predicate;
  private boolean filled = false;
  private final List<T> cache = CollectionUtil.newDefaultList();


  public DbQueryCache(ElementPredicate<T> predicate)
  {
    this.predicate = Assert.notNull(predicate);
  }


  public boolean isFilled()
  {
    return filled;
  }


  public void setFilled()
  {
    filled = true;
  }


  public void addResults(Collection<T> result)
  {
    for (T element : cache)
    {
      if (predicate.matches(element))
      {
        result.add(element);
      }
    }
  }


  public <KeyType> void addResults(Map<KeyType, T> result,
      MethodInvocation invocation)
  {
    for (T element : cache)
    {
      if (predicate.matches(element))
      {
        @SuppressWarnings("unchecked")
        KeyType key = (KeyType) getKey(element, invocation);
        result.put(key, element);
      }
    }
  }


  private Object getKey(T element, MethodInvocation invocation)
  {
    return invocation.invoke(element);
  }


  public void add(T element)
  {
    cache.add(element);
  }

}
