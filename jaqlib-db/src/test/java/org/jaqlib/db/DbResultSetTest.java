package org.jaqlib.db;

import org.easymock.EasyMock;
import org.jaqlib.AccountSetup;
import org.jaqlib.DatabaseSetup;
import org.jaqlib.db.sql.typehandler.DefaultSqlTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DbResultSetTest
{

  private ResultSet rs;
  private DefaultSqlTypeHandlerRegistry sqlTypeHandlerRegistry;

  private DbResultSet resultSet;


  @BeforeEach
  public void setUp() throws Exception
  {
    rs = DatabaseSetup.getMockResultSet();
    sqlTypeHandlerRegistry = new DefaultSqlTypeHandlerRegistry();
    resultSet = new DbResultSet(rs, sqlTypeHandlerRegistry, DbDefaults.INSTANCE
            .getStrictFieldCheck());
  }

  @Test
  public void testDbResultSet_Null() throws SQLException
  {
    invalidConstructorArgs(null, DbDefaults.INSTANCE
            .getSqlTypeHandlerRegistry(), false);
    invalidConstructorArgs(rs, null, false);
  }


  private void invalidConstructorArgs(ResultSet rs,
                                      SqlTypeHandlerRegistry registry,
                                      boolean strictColumnCheck)
          throws SQLException
  {
    try
    {
      new DbResultSet(rs, registry, strictColumnCheck);
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
    assertTrue(resultSet.hasColumn("lastname"));
    assertTrue(resultSet.hasColumn("lastName"));
    assertTrue(resultSet.hasColumn("LASTNAME"));
    assertTrue(resultSet.hasColumn("Lastname"));
    assertFalse(resultSet.hasColumn("nonExisting"));
  }

  @Test
  public void testClose() throws SQLException
  {
    EasyMock.reset(rs);
    rs.close();
    EasyMock.replay(rs);

    resultSet.close();

    EasyMock.verify(rs);
  }

  @Test
  public void testNext() throws SQLException
  {
    EasyMock.reset(rs);
    EasyMock.expect(rs.next()).andReturn(Boolean.FALSE);
    EasyMock.replay(rs);

    resultSet.next();

    EasyMock.verify(rs);
  }

  @Test
  public void testGetObject_ColumnName()
  {
    ColumnMapping<?> mapping = new ColumnMapping<>("lastName");
    Object value = resultSet.getObject(mapping);
    assertEquals(AccountSetup.HUBER_ACCOUNT.getLastName(), value);
  }

  @Test
  public void testGetObject_ColumnIndex()
  {
    ColumnMapping<?> mapping = new ColumnMapping<>(1);
    Object value = resultSet.getObject(mapping);
    assertEquals(AccountSetup.HUBER_ACCOUNT.getId(), value);
  }

  @Test
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

    ColumnMapping<?> mapping = new ColumnMapping<>(1);
    mapping.setColumnDataType(sqlDataType);
    resultSet.getObject(mapping);

    EasyMock.verify(th);
  }

}
