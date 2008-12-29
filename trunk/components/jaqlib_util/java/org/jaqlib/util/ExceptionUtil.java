package org.jaqlib.util;

public class ExceptionUtil
{

  public static RuntimeException toRuntimeException(Throwable t)
  {
    if (t instanceof RuntimeException)
    {
      return (RuntimeException) t;
    }
    RuntimeException re = new RuntimeException(t);
    re.setStackTrace(t.getStackTrace());
    return re;
  }

}
