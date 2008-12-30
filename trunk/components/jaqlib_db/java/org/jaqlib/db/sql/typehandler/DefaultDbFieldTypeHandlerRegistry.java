package org.jaqlib.db.sql.typehandler;

import static org.jaqlib.util.CollectionUtil.newDefaultMap;

import java.sql.Types;
import java.util.Map;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public class DefaultDbFieldTypeHandlerRegistry implements DbFieldTypeHandlerRegistry
{

  private final Map<Integer, DbFieldTypeHandler> handlers = newDefaultMap();
  private final DbFieldTypeHandler defaultTypeHandler = new ObjectTypeHandler();


  /**
   * Default constructor that registers default type handler instances.
   */
  public DefaultDbFieldTypeHandlerRegistry()
  {
    registerDefaultHandlers();
  }


  private void registerDefaultHandlers()
  {
    registerTypeHandler(Types.OTHER, defaultTypeHandler);
    registerTypeHandler(Types.NUMERIC, BIGDECIMAL_TYPEHANDLER);
    registerTypeHandler(Types.DECIMAL, BIGDECIMAL_TYPEHANDLER);
    registerTypeHandler(Types.BIGINT, LONG_TYPEHANDLER);
    registerTypeHandler(Types.BOOLEAN, BOOLEAN_TYPEHANDLER);
    registerTypeHandler(Types.CHAR, STRING_TYPEHANDLER);
    registerTypeHandler(Types.DATE, DATE_TYPEHANDLER);
    registerTypeHandler(Types.DOUBLE, DOUBLE_TYPEHANDLER);
    registerTypeHandler(Types.FLOAT, FLOAT_TYPEHANDLER);
    registerTypeHandler(Types.INTEGER, INTEGER_TYPEHANDLER);
    registerTypeHandler(Types.REAL, DOUBLE_TYPEHANDLER);
    registerTypeHandler(Types.SMALLINT, SHORT_TYPEHANDLER);
    registerTypeHandler(Types.TIME, TIME_TYPEHANDLER);
    registerTypeHandler(Types.TIMESTAMP, TIMESTAMP_TYPEHANDLER);
    registerTypeHandler(Types.TINYINT, SHORT_TYPEHANDLER);
    registerTypeHandler(Types.VARCHAR, STRING_TYPEHANDLER);

    registerTypeHandler(Types.ARRAY, ARRAY_TYPEHANDLER);
    registerTypeHandler(Types.BINARY, BYTES_TYPEHANDLER);
    registerTypeHandler(Types.BLOB, BLOB_TYPEHANDLER);
    registerTypeHandler(Types.CLOB, CLOB_TYPEHANDLER);
    registerTypeHandler(Types.LONGNVARCHAR, NCHAR_TYPEHANDLER);
    registerTypeHandler(Types.LONGVARBINARY, LONG_VARBINARY_TYPEHANDLER);
    registerTypeHandler(Types.LONGVARCHAR, LONG_VARCHAR_TYPEHANDLER);
    registerTypeHandler(Types.NCHAR, NCHAR_TYPEHANDLER);
    registerTypeHandler(Types.NCLOB, NCLOB_TYPEHANDLER);
    registerTypeHandler(Types.NVARCHAR, NCHAR_TYPEHANDLER);

    registerTypeHandler(Types.REF, REF_TYPEHANDLER);
    registerTypeHandler(Types.ROWID, ROWID_TYPEHANDLER);
    registerTypeHandler(Types.SQLXML, SQLXML_TYPEHANDLER);
  }


  /**
   * {@inheritDoc}
   */
  public DbFieldTypeHandler getTypeHandler(int dbDataType)
  {
    DbFieldTypeHandler handler = handlers.get(dbDataType);
    if (handler != null)
    {
      return handler;
    }
    else
    {
      return defaultTypeHandler;
    }
  }


  /**
   * {@inheritDoc}
   */
  public void registerTypeHandler(int dbDataType, DbFieldTypeHandler typeHandler)
  {
    Assert.notNull(typeHandler);
    handlers.put(dbDataType, typeHandler);
  }


}
