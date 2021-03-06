package org.jaqlib.core;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public class SimpleWhereCondition<T, DataSourceType, ResultType> extends
    QueryItem<T, DataSourceType> implements WhereCondition<T>,
    SingleElementWhereCondition<T, DataSourceType, ResultType>,
    LoggableQueryItem
{

  private ElementWhereCondition<T, DataSourceType, ResultType> element;


  public SimpleWhereCondition(Query<T, DataSourceType> query)
  {
    super(query);
  }


  public ComparableWhereCondition<T, DataSourceType, ResultType> element()
  {
    element = new ElementWhereCondition<>(getQuery());
    return element;
  }


  public boolean evaluate(T element)
  {
    return this.element.evaluate(element);
  }


  public void appendLogString(StringBuilder sb)
  {
    element.appendLogString(sb);
  }

}
