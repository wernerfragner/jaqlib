package org.jaqlib.query;

import org.jaqlib.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public abstract class ReflectiveCompare<T, ResultType> extends
    AbstractCompare<T, ResultType>
{

  private final MethodInvocation invocation;


  public ReflectiveCompare(MethodInvocation invocation, ResultType expected)
  {
    super(expected);
    this.invocation = invocation;
  }


  protected boolean invocationPresent()
  {
    return invocation != null;
  }


  @SuppressWarnings("unchecked")
  protected ResultType getActualValue(T item)
  {
    // invocations on null objects do not make sense, return null
    if (item == null)
    {
      return null;
    }

    // if no invocation has been made than item itself should be evaluated
    if (invocation == null)
    {
      return (ResultType) item;
    }

    // invoke recored method on given item
    return (ResultType) invocation.invoke(item);
  }
}
