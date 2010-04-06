package org.jaqlib.core;

import java.util.Collection;
import java.util.Map;

import org.jaqlib.core.bean.AbstractMapping;
import org.jaqlib.core.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T> the element type of the query.
 */
public abstract class AbstractFetchStrategy<T>
{

  private ElementPredicate<T> predicate;
  private SelectDataSource dataSource;
  private AbstractMapping<T> mapping;


  public void setPredicate(ElementPredicate<T> predicate)
  {
    this.predicate = predicate;
  }


  protected ElementPredicate<T> getPredicate()
  {
    return predicate;
  }


  public void setDataSource(SelectDataSource dataSource)
  {
    this.dataSource = dataSource;
  }


  public void setMapping(AbstractMapping<T> mapping)
  {
    this.mapping = mapping;
  }


  public void addResults(Collection<T> results)
  {
    try
    {
      final DsResultSet rs = queryDataSource();

      boolean stop = false;
      while (!stop && rs.next())
      {
        final T element = extractElement(rs);
        if (shouldAddToResult(element))
        {
          results.add(element);
          stop = elementProcessed(element, true);
        }
        else
        {
          stop = elementProcessed(element, false);
        }
      }
    }
    finally
    {
      getDataSource().closeAfterQuery();
    }
  }


  protected abstract boolean elementProcessed(T element, boolean isMatch);


  private boolean shouldAddToResult(T element)
  {
    return predicate.matches(element);
  }


  private T extractElement(DsResultSet rs)
  {
    return mapping.getValue(rs);
  }


  private DsResultSet queryDataSource()
  {
    return getDataSource().execute();
  }


  private SelectDataSource getDataSource()
  {
    return dataSource;
  }


  public <KeyType> void addResults(Map<KeyType, T> results,
      MethodInvocation invocation)
  {
    try
    {
      final DsResultSet rs = queryDataSource();

      boolean stop = false;
      while (!stop && rs.next())
      {
        final T element = extractElement(rs);
        if (element != null && shouldAddToResult(element))
        {
          @SuppressWarnings("unchecked")
          final KeyType elementKey = (KeyType) getKey(element, invocation);
          results.put(elementKey, element);

          stop = elementProcessed(element, true);
        }
        else
        {
          stop = elementProcessed(element, false);
        }
      }
    }
    finally
    {
      getDataSource().closeAfterQuery();
    }
  }


  private Object getKey(T element, MethodInvocation invocation)
  {
    return invocation.invoke(element);
  }


}
