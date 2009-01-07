package org.jaqlib.db;

import org.jaqlib.DatabaseQueryBuilder;
import org.jaqlib.core.WhereClause;
import org.jaqlib.util.Assert;

/**
 * FROM clause for database queries.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class FromClause<T>
{

  private final DatabaseQueryBuilder queryBuilder;
  private final AbstractMapping<T> mapping;


  public FromClause(DatabaseQueryBuilder queryBuilder,
      AbstractMapping<T> mapping)
  {
    this.queryBuilder = Assert.notNull(queryBuilder);
    this.mapping = Assert.notNull(mapping);
  }


  /**
   * Defines the data source for the query.
   * 
   * @param dataSource a not null data source for the query.
   * @return a where clause for defining the query conditions.
   */
  public WhereClause<T, DbSelectDataSource> from(DbSelectDataSource dataSource)
  {
    return this.getQuery().createWhereClause(dataSource);
  }


  private DatabaseQuery<T> getQuery()
  {
    return queryBuilder.createQuery(mapping);
  }

}
