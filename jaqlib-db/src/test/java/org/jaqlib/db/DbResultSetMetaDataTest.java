package org.jaqlib.db;

import junit.framework.TestCase;
import org.jaqlib.DatabaseSetup;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbResultSetMetaDataTest extends TestCase
{

  private ResultSet rs;
  private DbResultSetMetaData metaData;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    rs = DatabaseSetup.getMockResultSet();

    metaData = new DbResultSetMetaData(rs.getMetaData());
  }


  public void testDbResultSetMetaData()
  {
    try
    {
      new DbResultSetMetaData(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testHasColumn() throws SQLException
  {
    assertTrue(metaData.hasColumn("id"));
    assertTrue(metaData.hasColumn("lastName"));
    assertTrue(metaData.hasColumn("lastname"));
    assertTrue(metaData.hasColumn("LASTNAME"));
    assertTrue(metaData.hasColumn("LastName"));
    assertFalse(metaData.hasColumn("notpresent"));
  }

}
