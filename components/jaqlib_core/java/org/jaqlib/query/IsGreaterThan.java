package org.jaqlib.query;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class IsGreaterThan<T> extends ComparableFunction<T>
{

  public IsGreaterThan(T expected)
  {
    super(expected);
  }


  @Override
  protected boolean doCompare(Comparable<T> actual, T expected)
  {
    return actual.compareTo(expected) > 0;
  }

}
