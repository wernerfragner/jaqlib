package org.jaqlib.db.sql.typehandler;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Werner Fragner
 */
public class LongTypeHandler implements DbFieldTypeHandler
{

  public Object getObject(ResultSet resultSet, String columnLabel)
      throws SQLException
  {
    return resultSet.getLong(columnLabel);
  }


  public Object getObject(ResultSet resultSet, int columnIndex)
      throws SQLException
  {
    return resultSet.getLong(columnIndex);
  }

}
