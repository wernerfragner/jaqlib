package org.jaqlib;

import junit.framework.TestCase;

import org.jaqlib.db.DbDefaults;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;

public class DbDefaultsTest extends TestCase
{

  @Override
  public void tearDown()
  {
    DbDefaults.INSTANCE.reset();
  }


  public void testGetSqlTypeHandlerRegistry()
  {
    assertNotNull(DbDefaults.INSTANCE.getSqlTypeHandlerRegistry());
  }


  public void testSetSqlTypeHandlerRegistry_Null()
  {
    try
    {
      DbDefaults.INSTANCE.setSqlTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testSetSqlTypeHandlerRegistry()
  {
    SqlTypeHandlerRegistry registry = new SqlTypeHandlerRegistry()
    {

      public SqlTypeHandler getTypeHandler(int sqlDataType)
      {
        return null;
      }


      public void registerTypeHandler(int sqlDataType,
          SqlTypeHandler typeHandler)
      {
      }

    };
    DbDefaults.INSTANCE.setSqlTypeHandlerRegistry(registry);
    assertSame(registry, DbDefaults.INSTANCE.getSqlTypeHandlerRegistry());
  }


}
