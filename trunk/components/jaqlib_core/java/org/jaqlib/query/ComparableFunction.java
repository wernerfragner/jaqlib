package org.jaqlib.query;

import org.jaqlib.util.reflect.ReflectionUtil;


/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public abstract class ComparableFunction<T> extends AbstractCompare<T, T>
{

  public ComparableFunction(T expected)
  {
    super(expected);
  }


  public boolean evaluate(T item)
  {
    if (item instanceof Comparable)
    {
      Comparable<T> actualComp = (Comparable<T>) item;
      return doCompare(actualComp, expected);
    }
    else if (item == null)
    {
      return false;
    }
    throw new IllegalArgumentException(getPlainClassName()
        + "() can only be used if the item class " + "implements '"
        + Comparable.class.getName() + "'.");
  }


  protected abstract boolean doCompare(Comparable<T> actual, T expected);


  private String getPlainClassName()
  {
    return ReflectionUtil.getPlainClassName(this);
  }

}
