package org.jaqlib.db;

import org.jaqlib.DatabaseSetup;
import org.jaqlib.util.ExceptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DbSelectDataSourceTest
{

  private static final String SQL = DatabaseSetup.SELECT_SQL;
  private static final String SELECT_SQL_WHERE = DatabaseSetup.SELECT_SQL_WHERE;

  private DbSelectDataSource dataSource;


  @BeforeEach
  protected void setUp()
  {
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

  @Test
  public void testDbSelectDataSource_Null()
  {
    try
    {
      new DbSelectDataSource(null, SQL);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
    try
    {
      new DbSelectDataSource(getDataSource(), null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testRegisterSqlTypeHandler_Null()
  {
    try
    {
      dataSource.registerSqlTypeHandler(1, null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testSetSqlTypeHandlerRegistry_Null()
  {
    try
    {
      dataSource.setSqlTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testGetSql()
  {
    assertEquals(SQL, dataSource.getSql());
  }

  @Test
  public void testGetSql_WithWhereCondition()
  {
    dataSource.setSqlWhereCondition(SELECT_SQL_WHERE);
    assertEquals(SQL + " WHERE " + SELECT_SQL_WHERE, dataSource.getSql());
  }

  @Test
  public void testExecute_NormalStatement()
  {
    DbResultSet rs = dataSource.execute();
    assertNotNull(rs);
    assertNotSame(rs, dataSource.execute());
  }

  @Test
  public void testExecute_PreparedStatement()
  {
    dataSource.addPreparedStatementParameter(5000);
    DbResultSet rs = dataSource.execute();
    assertNotNull(rs);
    assertNotSame(rs, dataSource.execute());
  }

  @Test
  public void testClose_NoExecutePerformed()
  {
    dataSource.closeAfterQuery();
  }

  @Test
  public void testClose_ExecutePerformed()
  {
    dataSource.execute();
    dataSource.closeAfterQuery();
  }

}
