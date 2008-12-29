package org.jaqlib.query.db;

import org.jaqlib.db.DbSelect;
import org.jaqlib.db.DbSelectResult;
import org.jaqlib.query.AbstractQueryBuilder;
import org.jaqlib.query.FromClause;

/**
 * @author Werner Fragner
 */
public class DatabaseQueryBuilder extends AbstractQueryBuilder
{

  public <T> FromClause<T, DbSelect> select(DbSelectResult<T> result)
  {
    return this.<T> createQuery().createFromClause(result);
  }


  private <T> DatabaseQuery<T> createQuery()
  {
    return new DatabaseQuery<T>(getMethodCallRecorder());
  }

}
