package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class SimpleTestElementImpl implements SimpleTestElement
{

  private boolean match = false;
  private Object object = null;
  private Integer compareValue = 0;


  public SimpleTestElementImpl()
  {
  }


  public SimpleTestElementImpl(boolean match)
  {
    this.match = match;
  }


  public SimpleTestElementImpl(Integer compareValue)
  {
    this.compareValue = compareValue;
  }


  public SimpleTestElementImpl(Object object)
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


  public int compareTo(SimpleTestElement o)
  {
    if (compareValue == null)
    {
      return (o.getCompareValue() == null ? 0 : -1);
    }
    return compareValue.compareTo(o.getCompareValue());
  }


  @Override
  public String toString()
  {
    return getClass().getName() + ": compareValue=" + compareValue + "; match="
        + match + "; object=" + object;
  }


}
