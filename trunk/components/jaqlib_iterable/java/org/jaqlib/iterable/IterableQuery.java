package org.jaqlib.iterable;

import java.util.Collection;
import java.util.Map;

import org.jaqlib.core.AbstractQuery;
import org.jaqlib.core.WhereClause;
import org.jaqlib.core.reflect.MethodCallRecorder;
import org.jaqlib.core.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T> the result element class of the query.
 */
public class IterableQuery<T> extends AbstractQuery<T, Iterable<T>>
{

  public IterableQuery(MethodCallRecorder methodCallRecorder)
  {
    super(methodCallRecorder);
  }


  public WhereClause<T, Iterable<T>> createWhereClause(Iterable<T> dataSource)
  {
    setDataSource(dataSource);
    return new WhereClause<T, Iterable<T>>(this);
  }


  @Override
  protected void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    for (T element : getIterable())
    {
      if (tree.matches(element))
      {
        result.add(element);

        if (stopAtFirstMatch)
        {
          return;
        }
      }
    }
  }


  @Override
  protected <KeyType> void addResults(final Map<KeyType, T> resultMap)
  {
    final MethodInvocation invocation = getCurrentInvocation();
    for (T element : getIterable())
    {
      if (element != null && tree.matches(element))
      {
        @SuppressWarnings("unchecked")
        final KeyType elementKey = (KeyType) getKey(element, invocation);
        resultMap.put(elementKey, element);
      }
    }
  }


  private Iterable<T> getIterable()
  {
    return getDataSource();
  }


  @Override
  protected String getResultDefinitionString()
  {
    return "";
  }

}
