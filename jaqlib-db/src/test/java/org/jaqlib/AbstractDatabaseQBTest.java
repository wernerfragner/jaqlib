package org.jaqlib;

import junit.framework.TestCase;

import javax.sql.DataSource;

public class AbstractDatabaseQBTest extends TestCase
{

  protected DatabaseSetup dbSetup;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    dbSetup = new DatabaseSetup();
    dbSetup.createTestTables();
    dbSetup.insertTestRecords();
  }


  @Override
  public void tearDown() throws Exception
  {
    super.tearDown();

    dbSetup.clear();
  }


  protected DataSource getDataSource()
  {
    return dbSetup.getDataSource();
  }

}
