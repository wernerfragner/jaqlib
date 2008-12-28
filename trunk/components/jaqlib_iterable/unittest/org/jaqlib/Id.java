package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class Id
{

  private static long current = 0;


  public synchronized static Long getNext()
  {
    current++;
    return current;
  }


  private Id()
  {
  }

}
