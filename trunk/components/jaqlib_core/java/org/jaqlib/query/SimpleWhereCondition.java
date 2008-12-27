package org.jaqlib.query;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 * @param <ResultType>
 */
public class SimpleWhereCondition<T, DataSourceType, ResultType> extends
    QueryItem<T, DataSourceType> implements WhereCondition<T>,
    SingleItemWhereCondition<T, DataSourceType, ResultType>
{

  private ItemWhereCondition<T, DataSourceType, ResultType> item;


  public SimpleWhereCondition(Query<T, DataSourceType> query)
  {
    super(query);
  }


  public ComparableWhereCondition<T, DataSourceType, ResultType> item()
  {
    item = new ItemWhereCondition<T, DataSourceType, ResultType>(getQuery());
    return item;
  }


  public boolean evaluate(T item)
  {
    return this.item.evaluate(item);
  }

}
