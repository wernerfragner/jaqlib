package org.jaqlib.core;

/**
 * Represents the FROM clause of the query.
 * 
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
   * @param dataSource a not null data source for the query.
   * @return a where clause for defining the query conditions.
   */
  public WhereClause<T, DataSourceType> from(DataSourceType dataSource)
  {
    return getQuery().createWhereClause(dataSource);
  }

}
