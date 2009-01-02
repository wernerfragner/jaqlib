package org.jaqlib.core;


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


}
