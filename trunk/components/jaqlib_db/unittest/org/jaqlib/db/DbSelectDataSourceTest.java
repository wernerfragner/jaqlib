package org.jaqlib.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.jaqlib.DatabaseSetup;
import org.jaqlib.util.ExceptionUtil;

public class DbSelectDataSourceTest extends TestCase
{

  private static final String SQL = DatabaseSetup.SELECT_SQL;

  private DbSelectDataSource dataSource;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    dataSource = new DbSelectDataSource(getDataSource(), SQL);
  }


  private DataSource getDataSource()
  {
    try
    {
      return DatabaseSetup.getNiceMockDataSource();
    }
    catch (SQLException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  public void testDbSelectDataSource_Null()
  {
    try
    {
      new DbSelectDataSource(null, SQL);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
    try
    {
      new DbSelectDataSource(getDataSource(), null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testRegisterSqlTypeHandler_Null()
  {
    try
    {
      dataSource.registerSqlTypeHandler(1, null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testSetSqlTypeHandlerRegistry_Null()
  {
    try
    {
      dataSource.setSqlTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testGetSql()
  {
    assertEquals(SQL, dataSource.getSql());
  }


  public void testExecute() throws SQLException
  {
    DbResultSet rs = dataSource.execute();
    assertNotNull(rs);
    assertNotSame(rs, dataSource.execute());
  }


  public void testClose_NoExecutePerformed()
  {
    dataSource.close();
  }


  public void testClose_ExecutePerformed() throws SQLException
  {
    dataSource.execute();
    dataSource.close();
  }

}
