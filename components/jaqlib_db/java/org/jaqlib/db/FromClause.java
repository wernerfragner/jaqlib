package org.jaqlib.db;

import javax.sql.DataSource;

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


  /**
   * Defines the data source for the query.
   * 
   * @param dataSource a not null data source for the query.
   * @param sql the SQL statement that should be executed.
   * @return a where clause for defining the query conditions.
   */
  public WhereClause<T, DbSelectDataSource> from(DataSource dataSource,
      String sql)
  {
    return from(new DbSelectDataSource(dataSource, sql));
  }


  private DatabaseQuery<T> getQuery()
  {
    return queryBuilder.createQuery(mapping);
  }

}
