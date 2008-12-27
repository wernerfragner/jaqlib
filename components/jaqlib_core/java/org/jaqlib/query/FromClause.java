package org.jaqlib.query;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 */
public class FromClause<T, DataSourceType> extends QueryItem<T, DataSourceType>
{

  public FromClause(Query<T, DataSourceType> query)
  {
    super(query);
  }


  /**
   * Defines the data source for the query.
   * 
   * @param dataSource
   * @return
   */
  public WhereClause<T, DataSourceType> from(DataSourceType dataSource)
  {
    return getQuery().createWhereClause(dataSource);
  }

}
