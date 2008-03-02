package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class NoDefaultConstructurClass
{

  private final Object someArg;


  public NoDefaultConstructurClass(Object someArg)
  {
    this.someArg = someArg;
  }


  public Object getSomeArg()
  {
    return someArg;
  }

}
