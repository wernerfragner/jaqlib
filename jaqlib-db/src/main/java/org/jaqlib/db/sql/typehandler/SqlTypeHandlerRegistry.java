package org.jaqlib.db.sql.typehandler;


/**
 * @author Werner Fragner
 */
public interface SqlTypeHandlerRegistry
{

  ObjectTypeHandler OBJECT_TYPEHANDLER = new ObjectTypeHandler();
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
   * @param sqlDataType the SQL data type as defined at {@link java.sql.Types}.
   * @return a type handler instance for the given SQL data type. Must not
   *         return null.
   */
  SqlTypeHandler getTypeHandler(int sqlDataType);


  /**
   * Registers the given custom SQL type handler with the given SQL data type.
   * 
   * @param sqlDataType a SQL data type as defined at {@link java.sql.Types}.
   * @param typeHandler a not null type handler.
   */
  void registerTypeHandler(int sqlDataType, SqlTypeHandler typeHandler);

}