package org.jaqlib.db.sql.typehandler;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class DefaultSqlTypeHandlerRegistryTest
{

  private DefaultSqlTypeHandlerRegistry registry;


  @BeforeEach
  public void setUp()
  {
    registry = new DefaultSqlTypeHandlerRegistry();
  }


  /**
   * No type handler for given data type available. An ObjectTypeHandler must be
   * returned.
   */
  @Test
  public void testGetTypeHandler_NoneAvailable()
  {
    assertSame(SqlTypeHandlerRegistry.OBJECT_TYPEHANDLER, registry
        .getTypeHandler(Integer.MAX_VALUE));
  }

  @Test
  public void testRegisterTypeHandler_Null()
  {
    try
    {
      registry.registerTypeHandler(Types.BIT, null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testRegisterTypeHandler()
  {
    int sqlDataType = Integer.MAX_VALUE;

    SqlTypeHandler th = EasyMock.createMock(SqlTypeHandler.class);
    registry.registerTypeHandler(sqlDataType, th);

    assertSame(th, registry.getTypeHandler(sqlDataType));
  }

}
