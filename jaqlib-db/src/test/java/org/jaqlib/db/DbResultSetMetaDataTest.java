package org.jaqlib.db;

import org.jaqlib.DatabaseSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DbResultSetMetaDataTest
{

  private ResultSet rs;
  private DbResultSetMetaData metaData;


  @BeforeEach
  protected void setUp() throws Exception
  {
    rs = DatabaseSetup.getMockResultSet();

    metaData = new DbResultSetMetaData(rs.getMetaData());
  }


  @Test
  public void testDbResultSetMetaData()
  {
    try
    {
      new DbResultSetMetaData(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
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
