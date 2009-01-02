package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.ReflectionUtil;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public abstract class ComparableFunction<T, ResultType> extends
    ReflectiveCompare<T, ResultType>
{

  public ComparableFunction(MethodInvocation invocation, ResultType expected)
  {
    super(invocation, expected);
  }


  @Override
  public boolean doEvaluate(ResultType actual)
  {
    if (actual instanceof Comparable)
    {
      // 'actual' is a Comparable and is a ResultType --> safe type cast
      @SuppressWarnings("unchecked")
      Comparable<ResultType> actualComp = (Comparable<ResultType>) actual;
      return doCompare(actualComp, expected);
    }
    else if (actual == null)
    {
      return false;
    }
    throw new IllegalArgumentException(getPlainClassName()
        + "() can only be used if the element class " + "implements '"
        + Comparable.class.getName() + "'.");
  }


  protected abstract boolean doCompare(Comparable<ResultType> actual,
      ResultType expected);


  private String getPlainClassName()
  {
    return ReflectionUtil.getPlainClassName(this);
  }

}
