package org.jaqlib.db.sql.typehandler;

import java.sql.Types;

import junit.framework.TestCase;

import org.easymock.EasyMock;

public class DefaultSqlTypeHandlerRegistryTest extends TestCase
{

  private DefaultSqlTypeHandlerRegistry registry;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    registry = new DefaultSqlTypeHandlerRegistry();
  }


  /**
   * No type handler for given data type available. An ObjectTypeHandler must be
   * returned.
   */
  public void testGetTypeHandler_NoneAvailable()
  {
    assertSame(SqlTypeHandlerRegistry.OBJECT_TYPEHANDLER, registry
        .getTypeHandler(Integer.MAX_VALUE));
  }


  public void testRegisterTypeHandler_Null()
  {
    try
    {
      registry.registerTypeHandler(Types.BIT, null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testRegisterTypeHandler()
  {
    int sqlDataType = Integer.MAX_VALUE;

    SqlTypeHandler th = EasyMock.createMock(SqlTypeHandler.class);
    registry.registerTypeHandler(sqlDataType, th);

    assertSame(th, registry.getTypeHandler(sqlDataType));
  }

}
