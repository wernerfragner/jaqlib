package org.jaqlib.util.db;

import org.jaqlib.util.db.typehandler.ArrayTypeHandler;
import org.jaqlib.util.db.typehandler.BigDecimalTypeHandler;
import org.jaqlib.util.db.typehandler.BlobTypeHandler;
import org.jaqlib.util.db.typehandler.BooleanTypeHandler;
import org.jaqlib.util.db.typehandler.BytesTypeHandler;
import org.jaqlib.util.db.typehandler.ClobTypeHandler;
import org.jaqlib.util.db.typehandler.DateTypeHandler;
import org.jaqlib.util.db.typehandler.DoubleTypeHandler;
import org.jaqlib.util.db.typehandler.FloatTypeHandler;
import org.jaqlib.util.db.typehandler.IntegerTypeHandler;
import org.jaqlib.util.db.typehandler.LongTypeHandler;
import org.jaqlib.util.db.typehandler.LongVarbinaryTypeHandler;
import org.jaqlib.util.db.typehandler.LongVarcharTypeHandler;
import org.jaqlib.util.db.typehandler.NCharTypeHandler;
import org.jaqlib.util.db.typehandler.NClobTypeHandler;
import org.jaqlib.util.db.typehandler.RefTypeHandler;
import org.jaqlib.util.db.typehandler.RowIdTypeHandler;
import org.jaqlib.util.db.typehandler.ShortTypeHandler;
import org.jaqlib.util.db.typehandler.SqlXmlTypeHandler;
import org.jaqlib.util.db.typehandler.StringTypeHandler;
import org.jaqlib.util.db.typehandler.TimeTypeHandler;
import org.jaqlib.util.db.typehandler.TimestampTypeHandler;

/**
 * @author Werner Fragner
 */
public interface TypeHandlerRegistry
{

  SqlXmlTypeHandler SQLXML_TYPEHANDLER = new SqlXmlTypeHandler();
  RowIdTypeHandler ROWID_TYPEHANDLER = new RowIdTypeHandler();
  RefTypeHandler REF_TYPEHANDLER = new RefTypeHandler();
  NClobTypeHandler NCLOB_TYPEHANDLER = new NClobTypeHandler();
  LongVarcharTypeHandler LONG_VARCHAR_TYPEHANDLER = new LongVarcharTypeHandler();
  LongVarbinaryTypeHandler LONG_VARBINARY_TYPEHANDLER = new LongVarbinaryTypeHandler();
  NCharTypeHandler NCHAR_TYPEHANDLER = new NCharTypeHandler();
  ClobTypeHandler CLOB_TYPEHANDLER = new ClobTypeHandler();
  BlobTypeHandler BLOB_TYPEHANDLER = new BlobTypeHandler();
  BytesTypeHandler BYTES_TYPEHANDLER = new BytesTypeHandler();
  ArrayTypeHandler ARRAY_TYPEHANDLER = new ArrayTypeHandler();
  TimestampTypeHandler TIMESTAMP_TYPEHANDLER = new TimestampTypeHandler();
  TimeTypeHandler TIME_TYPEHANDLER = new TimeTypeHandler();
  ShortTypeHandler SHORT_TYPEHANDLER = new ShortTypeHandler();
  IntegerTypeHandler INTEGER_TYPEHANDLER = new IntegerTypeHandler();
  FloatTypeHandler FLOAT_TYPEHANDLER = new FloatTypeHandler();
  DoubleTypeHandler DOUBLE_TYPEHANDLER = new DoubleTypeHandler();
  DateTypeHandler DATE_TYPEHANDLER = new DateTypeHandler();
  StringTypeHandler STRING_TYPEHANDLER = new StringTypeHandler();
  BooleanTypeHandler BOOLEAN_TYPEHANDLER = new BooleanTypeHandler();
  LongTypeHandler LONG_TYPEHANDLER = new LongTypeHandler();
  BigDecimalTypeHandler BIGDECIMAL_TYPEHANDLER = new BigDecimalTypeHandler();


  /**
   * @param dbDataType the DB data type as defined at {@link java.sql.Types}.
   * @return a type handler instance for the given DB data type. Must not return
   *         null.
   */
  TypeHandler getTypeHandler(int dbDataType);


  /**
   * Registers a custom type handler with a DB data type.
   * 
   * @param dbDataType a DB data type as defined at {@link java.sql.Types}.
   * @param typeHandler a custom type handler instance; must not be null.
   */
  void registerTypeHandler(int dbDataType, TypeHandler typeHandler);

}