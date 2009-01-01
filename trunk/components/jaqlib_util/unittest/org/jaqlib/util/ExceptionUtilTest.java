package org.jaqlib.util;

import junit.framework.TestCase;

public class ExceptionUtilTest extends TestCase
{


  public void testToRuntimeException_RuntimeException()
  {
    RuntimeException e = new RuntimeException();
    assertSame(e, ExceptionUtil.toRuntimeException(e));
  }


  public void testToRuntimeException_CheckedException()
  {
    NoSuchMethodException e = new NoSuchMethodException();

    RuntimeException re = ExceptionUtil.toRuntimeException(e);
    assertSame(e, re.getCause());
  }

}
