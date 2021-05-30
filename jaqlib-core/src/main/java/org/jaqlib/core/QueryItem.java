package org.jaqlib.core;

import org.jaqlib.util.Assert;

/**
 * Abstract base class for all items of a query.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 */
public abstract class QueryItem<T, DataSourceType>
{

  private final Query<T, DataSourceType> query;


  public QueryItem(Query<T, DataSourceType> query)
  {
    this.query = Assert.notNull(query);
  }


  protected Query<T, DataSourceType> getQuery()
  {
    return query;
  }

}
