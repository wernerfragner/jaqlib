package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodCallRecorder;

/**
 * Abstract base class for classes that can create {@link Query} objects.
 * 
 * @author Werner Fragner
 */
public class AbstractQueryFactory
{

  protected final MethodCallRecorder methodCallRecorder;


  public AbstractQueryFactory(MethodCallRecorder methodCallRecorder)
  {
    super();
    this.methodCallRecorder = methodCallRecorder;
  }

}
