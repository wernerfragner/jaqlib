package org.jaqlib;

import org.jaqlib.db.DbDefaults;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DbDefaultsTest
{

  @AfterEach
  public void tearDown()
  {
    DbDefaults.INSTANCE.reset();
  }

  @Test
  public void testGetSqlTypeHandlerRegistry()
  {
    assertNotNull(DbDefaults.INSTANCE.getSqlTypeHandlerRegistry());
  }

  @Test
  public void testSetSqlTypeHandlerRegistry_Null()
  {
    try
    {
      DbDefaults.INSTANCE.setSqlTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
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
