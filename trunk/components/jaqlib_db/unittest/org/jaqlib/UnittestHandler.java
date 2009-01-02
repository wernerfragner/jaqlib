package org.jaqlib;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class UnittestHandler extends Handler
{

  public UnittestHandler()
  {
    setLevel(Level.ALL);
  }


  @Override
  public void close() throws SecurityException
  {
  }


  @Override
  public void flush()
  {
  }


  @Override
  public void publish(LogRecord record)
  {
  }

}
