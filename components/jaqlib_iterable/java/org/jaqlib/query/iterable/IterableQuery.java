package org.jaqlib.query.iterable;

import java.util.Collection;
import java.util.Map;

import org.jaqlib.query.AbstractQuery;
import org.jaqlib.query.FromClause;
import org.jaqlib.util.reflect.MethodCallRecorder;
import org.jaqlib.util.reflect.MethodInvocation;

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


  /**
   * The resultElementClass is only needed for determining the result element
   * type.
   * 
   * @param resultElementClass
   * @return an object representing a from clause.
   */
  public FromClause<T, Iterable<T>> createFromClause(Class<T> resultElementClass)
  {
    return new FromClause<T, Iterable<T>>(this);
  }


  @Override
  protected void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    for (T element : getDataSource())
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
    for (T element : getDataSource())
    {
      if (element != null && tree.matches(element))
      {
        final KeyType elementKey = getKey(element, invocation);
        resultMap.put(elementKey, element);
      }
    }
  }


}
