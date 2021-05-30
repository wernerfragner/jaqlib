package org.jaqlib.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ExceptionUtilTest
{

  @Test
  void testToRuntimeException_RuntimeException()
  {
    RuntimeException e = new RuntimeException();
    assertSame(e, ExceptionUtil.toRuntimeException(e));
  }

  @Test
  void testToRuntimeException_CheckedException()
  {
    NoSuchMethodException e = new NoSuchMethodException();

    RuntimeException re = ExceptionUtil.toRuntimeException(e);
    assertSame(e, re.getCause());
  }

}
