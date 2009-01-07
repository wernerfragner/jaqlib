package org.jaqlib.iterable;

import org.jaqlib.IterableQueryBuilder;
import org.jaqlib.core.WhereClause;
import org.jaqlib.util.Assert;


/**
 * FROM clause for queries on {@link Iterable} objects.
 * 
 * @author Werner Fragner
 */
public class FromClause
{

  private final IterableQueryBuilder queryBuilder;


  public FromClause(IterableQueryBuilder queryBuilder)
  {
    this.queryBuilder = Assert.notNull(queryBuilder);
  }


  /**
   * Defines the data source for the query.
   * 
   * @param iterable a not null Iterable for the query.
   * @return a where clause for defining the query conditions.
   */
  public <T> WhereClause<T, Iterable<T>> from(Iterable<T> iterable)
  {
    return this.<T> getQuery().createWhereClause(iterable);
  }


  private <T> IterableQuery<T> getQuery()
  {
    return queryBuilder.createQuery();
  }
}
