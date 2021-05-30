package org.jaqlib.db;

import org.jaqlib.util.Assert;

import javax.sql.DataSource;

/**
 * FROM clause for database queries.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class DbFromClause<T>
{

  private final DbQuery<T> query;


  public DbFromClause(DbQuery<T> query)
  {
    this.query = Assert.notNull(query);
  }


  /**
   * Defines the data source for the query.
   * 
   * @param dataSource a not null data source for the query.
   * @return a where clause for defining the query conditions.
   */
  public DbWhereClause<T> from(DbSelectDataSource dataSource)
  {
    return query.createDbWhereClause(dataSource);
  }


  /**
   * Defines the data source for the query.
   * 
   * @param dataSource a not null data source for the query.
   * @param sql the SQL statement that should be executed.
   * @return a where clause for defining the query conditions.
   */
  public DbWhereClause<T> from(DataSource dataSource, String sql)
  {
    return from(new DbSelectDataSource(dataSource, sql));
  }

}
