package org.jaqlib.query;

import org.jaqlib.reflect.MethodCallRecorder;
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

  private final MethodCallRecorder invocationRecorder;


  public ReflectiveWhereCondition(Query<T, DataSourceType> query,
      MethodCallRecorder invocationRecorder)
  {
    super(query);
    this.invocationRecorder = Assert.notNull(invocationRecorder);
  }


  @Override
  protected MethodInvocation getCurrentMethodInvocation()
  {
    return invocationRecorder.getCurrentInvocation();
  }


}
