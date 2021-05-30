package org.jaqlib.db;

import javax.sql.DataSource;

/**
 * @author Werner Fragner
 */
public class DeleteFromClause
{

  /**
   * Defines the table from which records should be deleted.
   * 
   * @param ds holds information which records of a table should be deleted.
   * @return the number of deleted records.
   */
  public int from(DbDeleteDataSource ds)
  {
    return ds.execute();
  }


  /**
   * Defines the table from which records should be deleted.
   * 
   * @param dataSource the data source holding the database connection.
   * @param tableName the name of the table from which records should be
   *          deleted.
   * @param whereClause the where clause that should be applied to the DELETE
   *          statement.
   * @return the number of deleted records.
   */
  public int from(DataSource dataSource, String tableName, String whereClause)
  {
    return from(new DbDeleteDataSource(dataSource, tableName, whereClause));
  }


  /**
   * Defines the table from which records should be deleted.
   * 
   * @param dataSource the data source holding the database connection.
   * @param tableName the name of the table from which records should be
   *          deleted.
   * @return the number of deleted records.
   */
  public int from(DataSource dataSource, String tableName)
  {
    return from(dataSource, tableName, null);
  }

}
