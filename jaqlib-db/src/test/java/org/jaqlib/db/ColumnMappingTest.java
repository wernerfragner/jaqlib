package org.jaqlib.db;

import org.jaqlib.AccountSetup;
import org.jaqlib.DatabaseSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColumnMappingTest
{

  private ColumnMapping<String> mapping;


  @BeforeEach
  public void setUp()
  {
    mapping = new ColumnMapping<>();
  }

  @Test
  public void testGetValue_ColumnIndexAvailable() throws SQLException
  {
    final String lastName = AccountSetup.HUBER_ACCOUNT.getLastName();
    DbResultSet rs = DatabaseSetup.getMockDbResultSet();

    mapping.setColumnIndex(2);
    Object value = mapping.getValue(rs);
    assertEquals(lastName, value);
  }

  @Test
  public void testGetValue_ColumnLabelAvailable() throws SQLException
  {
    final String lastName = AccountSetup.HUBER_ACCOUNT.getLastName();
    DbResultSet rs = DatabaseSetup.getMockDbResultSet();

    mapping.setColumnLabel("lastName");
    Object value = mapping.getValue(rs);
    assertEquals(lastName, value);
  }

}
