package org.jaqlib.util;

import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LogUtilTest
{

  @Test
  void testCustumLogHandler()
  {
    TestHandler handler = registerTestHandler(Level.WARNING);

    try
    {
      Logger logger = LogUtil.getLogger(LogUtilTest.class);

      logger.warning("This is a warning test for logging");
      logger.info("This is a info test for logging");

      // only warning message must have been logged
      assertEquals(1, handler.getNrRecords());
    }
    finally
    {
      unregisterTestHandler(handler);
    }
  }


  private void unregisterTestHandler(TestHandler handler)
  {
    LogUtil.unregisterLogHandler(handler);
  }


  private TestHandler registerTestHandler(Level level)
  {
    TestHandler handler = new TestHandler();
    LogUtil.registerLogHandler(handler, level);
    return handler;
  }

}
