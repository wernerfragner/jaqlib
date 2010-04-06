package org.jaqlib.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.jaqlib.AccountSetup;
import org.jaqlib.DatabaseSetup;
import org.jaqlib.core.Defaults;
import org.jaqlib.db.sql.typehandler.DefaultSqlTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;

public class DbResultSetTest extends TestCase
{

  private ResultSet rs;
  private DefaultSqlTypeHandlerRegistry sqlTypeHandlerRegistry;

  private DbResultSet resultSet;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    rs = DatabaseSetup.getMockResultSet();
    sqlTypeHandlerRegistry = new DefaultSqlTypeHandlerRegistry();
    resultSet = new DbResultSet(rs, sqlTypeHandlerRegistry, Defaults
        .getStrictColumnCheck());
  }


  public void testDbResultSet_Null() throws SQLException
  {
    invalidConstructurArgs(null, DbDefaults.getSqlTypeHandlerRegistry(), false);
    invalidConstructurArgs(rs, null, false);
  }


  private void invalidConstructurArgs(ResultSet rs,
      SqlTypeHandlerRegistry registry, boolean strictColumnCheck)
      throws SQLException
  {
    try
    {
      new DbResultSet(rs, registry, strictColumnCheck);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testHasColumn() throws SQLException
  {
    assertTrue(resultSet.hasColumn("lastname"));
    assertTrue(resultSet.hasColumn("lastName"));
    assertTrue(resultSet.hasColumn("LASTNAME"));
    assertTrue(resultSet.hasColumn("Lastname"));
    assertFalse(resultSet.hasColumn("nonExisting"));
  }


  public void testClose() throws SQLException
  {
    EasyMock.resetToNice(rs);
    rs.close();
    EasyMock.replay(rs);

    resultSet.close();

    EasyMock.verify(rs);
  }


  public void testNext() throws SQLException
  {
    EasyMock.resetToNice(rs);
    EasyMock.expect(rs.next()).andReturn(Boolean.FALSE);
    EasyMock.replay(rs);

    resultSet.next();

    EasyMock.verify(rs);
  }


  public void testGetObject_ColumnName() throws SQLException
  {
    Object value = resultSet.getObject(Types.OTHER, "lastName");
    assertEquals(AccountSetup.HUBER_ACCOUNT.getLastName(), value);
  }


  public void testGetObject_ColumnIndex() throws SQLException
  {
    Object value = resultSet.getObject(Types.OTHER, 1);
    assertEquals(AccountSetup.HUBER_ACCOUNT.getId(), value);
  }


  public void testGetObject_SqlTypeHandler() throws SQLException
  {
    // getObject() must be called on custom type handler

    SqlTypeHandler th = EasyMock.createStrictMock(SqlTypeHandler.class);
    EasyMock.expect(th.getObject(rs, 1)).andReturn(null);
    EasyMock.replay(th);

    // register type handler

    final int sqlDataType = Integer.MAX_VALUE;
    sqlTypeHandlerRegistry.registerTypeHandler(sqlDataType, th);

    // get object from result set

    resultSet.getObject(sqlDataType, 1);

    EasyMock.verify(th);
  }

}
