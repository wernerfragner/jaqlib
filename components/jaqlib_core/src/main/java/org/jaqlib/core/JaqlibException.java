package org.jaqlib.core;

/**
 * Base class for all execeptions thrown by Jaqlib.
 * 
 * @author Werner Fragner
 */
public class JaqlibException extends RuntimeException
{

  public JaqlibException()
  {
    super();
  }


  public JaqlibException(String message, Throwable cause)
  {
    super(message, cause);
  }


  public JaqlibException(String message)
  {
    super(message);
  }


  public JaqlibException(Throwable cause)
  {
    super(cause);
  }


}
