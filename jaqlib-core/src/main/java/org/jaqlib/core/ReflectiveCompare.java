package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;

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
  protected ResultType getActualValue(T element)
  {
    // invocations on null objects do not make sense, return null
    if (element == null)
    {
      return null;
    }

    // if no invocation has been made than element itself should be evaluated
    if (invocation == null)
    {
      return (ResultType) element;
    }

    // invoke recored method on given element
    return (ResultType) invocation.invoke(element);
  }


  public boolean evaluate(T element)
  {
    ResultType actual = getActualValue(element);
    return doEvaluate(actual);
  }


  protected abstract boolean doEvaluate(ResultType actual);


  @Override
  public void appendLogString(StringBuilder sb)
  {
    if (invocation != null)
    {
      sb.append(invocation);
      sb.append(".");
    }
    super.appendLogString(sb);
  }

}
