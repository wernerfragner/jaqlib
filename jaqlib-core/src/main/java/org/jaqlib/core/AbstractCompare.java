package org.jaqlib.core;

import org.jaqlib.util.ReflectionUtil;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public abstract class AbstractCompare<T, ResultType> implements
    Compare<T, ResultType>
{

  protected final ResultType expected;


  public AbstractCompare(ResultType expected)
  {
    this.expected = expected;
  }


  public void appendLogString(StringBuilder sb)
  {
    sb.append(ReflectionUtil.getPlainClassName(this));
    sb.append("(");
    if (expected != null)
    {
      sb.append(expected);
    }
    sb.append(")");
  }

}
