package org.jaqlib.query;

import org.jaqlib.reflect.MethodInvocation;

/**
 * Implementation of the {@link WhereCondition} interface that matches the
 * single elements with a user-defined WHERE clause.
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
  protected MethodInvocation getLastMethodInvocation()
  {
    // no method invocations are supported by this class
    return null;
  }

}