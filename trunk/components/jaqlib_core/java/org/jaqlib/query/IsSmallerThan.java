package org.jaqlib.query;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class IsSmallerThan<T> extends ComparableFunction<T>
{

  public IsSmallerThan(T expected)
  {
    super(expected);
  }


  @Override
  protected boolean doCompare(Comparable<T> actual, T expected)
  {
    return actual.compareTo(expected) < 0;
  }


}
