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

  private final DatabaseQBProperties properties;


  public DatabaseQueryBuilder()
  {
    this(new DatabaseQBProperties());
  }


  public DatabaseQueryBuilder(DatabaseQBProperties properties)
  {
    this.properties = properties;
  }


  public <T> FromClause<T, DbSelect> select(DbSelectResult<T> resultDefinition)
  {
    return this.<T> createQuery().createFromClause(resultDefinition);
  }


  private <T> DatabaseQuery<T> createQuery()
  {
    return new DatabaseQuery<T>(getMethodCallRecorder(), properties);
  }

}
