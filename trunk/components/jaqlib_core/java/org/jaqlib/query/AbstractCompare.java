package org.jaqlib.query;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <ResultType>
 */
public abstract class AbstractCompare<T, ResultType> implements Compare<T>
{

  protected final ResultType expected;


  public AbstractCompare(ResultType expected)
  {
    this.expected = expected;
  }


}
