package org.jaqlib.util.db.typehandler;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Werner Fragner
 */
public class FloatTypeHandler implements DbFieldTypeHandler
{

  public Object getObject(ResultSet resultSet, String columnLabel)
      throws SQLException
  {
    return resultSet.getFloat(columnLabel);
  }


  public Object getObject(ResultSet resultSet, int columnIndex)
      throws SQLException
  {
    return resultSet.getFloat(columnIndex);
  }

}
