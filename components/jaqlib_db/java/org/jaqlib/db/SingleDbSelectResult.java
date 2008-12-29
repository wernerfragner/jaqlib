package org.jaqlib.db;

/**
 * @author Werner Fragner
 */
public class SingleDbSelectResult<T> extends DbSelectResult<T>
{

  private static final int NO_INDEX = Integer.MIN_VALUE;

  private String name;
  private int index = NO_INDEX;


  public SingleDbSelectResult()
  {
  }


  public SingleDbSelectResult(String name)
  {
    this.name = name;
  }


  public SingleDbSelectResult(int index)
  {
    this.index = index;
  }


  public void setName(String name)
  {
    this.name = name;
  }


  public void setIndex(int index)
  {
    this.index = index;
  }


  public String getName()
  {
    return name;
  }


  public int getIndex()
  {
    return index;
  }


  public boolean hasName()
  {
    return name != null;
  }


  public boolean hasIndex()
  {
    // DB column index starts with 1
    return index > 0;
  }


}
