package org.jaqlib.query;

import org.jaqlib.reflect.JaqlibInvocationRecorder;
import org.jaqlib.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public class ReflectiveWhereCondition<T, DataSourceType, ResultType> extends
    AbstractComparableWhereCondition<T, DataSourceType, ResultType>
{

  private final JaqlibInvocationRecorder invocationRecorder;


  public ReflectiveWhereCondition(Query<T, DataSourceType> query,
      JaqlibInvocationRecorder invocationRecorder)
  {
    super(query);
    this.invocationRecorder = Assert.notNull(invocationRecorder);
  }


  @Override
  protected MethodInvocation getLastMethodInvocation()
  {
    return invocationRecorder.getLastInvocation();
  }


}
