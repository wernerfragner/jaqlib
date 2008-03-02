package org.jaqlib.query;



/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 */
public class WhereClause<T, DataSourceType> extends
    QueryResult<T, DataSourceType>
{

  public WhereClause(Query<T, DataSourceType> query)
  {
    super(query);
  }


  public <R> ReflectiveWhereCondition<T, DataSourceType, R> where(R evalResult)
  {
    return getQuery().addReflectiveWhereCondition();
  }


  public QueryResult<T, DataSourceType> where(WhereCondition<T> condition)
  {
    return getQuery().addWhereCondition(condition);
  }

}
