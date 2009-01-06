package org.jaqlib.db;

/**
 * @author Werner Fragner
 */
public class DeleteFromClause
{

  public int from(DbDeleteDataSource ds)
  {
    return ds.execute();
  }

}
