package org.jaqlib;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;

public abstract class AbstractDatabaseQBTest
{

  protected DatabaseSetup dbSetup;


  @BeforeEach
  public void setUp() throws Exception
  {
    dbSetup = new DatabaseSetup();
    dbSetup.createTestTables();
    dbSetup.insertTestRecords();
  }


  @AfterEach
  public void tearDown() throws Exception
  {
    dbSetup.clear();
  }


  protected DataSource getDataSource()
  {
    return dbSetup.getDataSource();
  }

}
