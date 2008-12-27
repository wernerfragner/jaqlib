package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class SimpleTestItemImpl implements SimpleTestItem
{

  private boolean match = false;
  private Object object = null;
  private Integer compareValue = 0;


  public SimpleTestItemImpl()
  {
  }


  public SimpleTestItemImpl(boolean match)
  {
    this.match = match;
  }


  public SimpleTestItemImpl(Integer compareValue)
  {
    this.compareValue = compareValue;
  }


  public SimpleTestItemImpl(Object object)
  {
    this(false);
    this.object = object;
  }


  public boolean isMatch()
  {
    return match;
  }


  public Object getObject()
  {
    return object;
  }


  public Integer getCompareValue()
  {
    return compareValue;
  }


  public int compareTo(SimpleTestItem o)
  {
    if (compareValue == null)
    {
      return (o.getCompareValue() == null ? 0 : -1);
    }
    return compareValue.compareTo(o.getCompareValue());
  }


}
