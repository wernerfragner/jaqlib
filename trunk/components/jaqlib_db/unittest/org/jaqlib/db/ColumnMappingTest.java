package org.jaqlib.db;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.jaqlib.AccountSetup;
import org.jaqlib.DatabaseSetup;
import org.jaqlib.core.bean.ColumnMapping;

public class ColumnMappingTest extends TestCase
{

  private ColumnMapping<String> mapping;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    mapping = new ColumnMapping<String>();
  }


  public void testGetValue_ColumnIndexAvailable() throws SQLException
  {
    final String lastName = AccountSetup.HUBER_ACCOUNT.getLastName();
    DbResultSet rs = DatabaseSetup.getMockDbResultSet();

    mapping.setColumnIndex(2);
    Object value = mapping.getValue(rs);
    assertEquals(lastName, value);
  }


  public void testGetValue_ColumnLabelAvailable() throws SQLException
  {
    final String lastName = AccountSetup.HUBER_ACCOUNT.getLastName();
    DbResultSet rs = DatabaseSetup.getMockDbResultSet();

    mapping.setColumnLabel("lastName");
    Object value = mapping.getValue(rs);
    assertEquals(lastName, value);
  }

}
