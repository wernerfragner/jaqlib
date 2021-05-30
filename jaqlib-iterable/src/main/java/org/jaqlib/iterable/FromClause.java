package org.jaqlib.iterable;

import org.jaqlib.core.WhereClause;
import org.jaqlib.util.Assert;


/**
 * FROM clause for queries on {@link Iterable} objects.
 * 
 * @author Werner Fragner
 */
public class FromClause
{

  private final IterableQueryFactory queryFactory;


  public FromClause(IterableQueryFactory queryFactory)
  {
    this.queryFactory = Assert.notNull(queryFactory);
  }


  /**
   * Defines the data source for the query.
   * 
   * @param iterable a not null Iterable for the query.
   * @return a where clause for defining the query conditions.
   */
  public <T> WhereClause<T, Iterable<T>> from(Iterable<T> iterable)
  {
    return this.<T> createQuery().createWhereClause(iterable);
  }


  private <T> IterableQuery<T> createQuery()
  {
    return queryFactory.createQuery();
  }

}
