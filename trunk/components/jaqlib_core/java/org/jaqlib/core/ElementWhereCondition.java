package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodInvocation;

/**
 * Implementation of the {@link WhereCondition} interface that matches the
 * single elements with a custom WHERE condition.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public class ElementWhereCondition<T, DataSourceType, ResultType> extends
    AbstractComparableWhereCondition<T, DataSourceType, ResultType>
{

  public ElementWhereCondition(Query<T, DataSourceType> query)
  {
    super(query);
  }


  @Override
  protected MethodInvocation getCurrentMethodInvocation()
  {
    // no method invocations are supported by this class
    return null;
  }

}
