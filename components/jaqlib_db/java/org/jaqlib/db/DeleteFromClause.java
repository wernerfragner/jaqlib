package org.jaqlib.db;

/**
 * @author Werner Fragner
 */
public class DeleteFromClause
{

  public void from(DbDeleteDataSource ds)
  {
    ds.execute();
  }

}
