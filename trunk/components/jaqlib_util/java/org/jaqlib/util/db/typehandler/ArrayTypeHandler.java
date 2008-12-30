package org.jaqlib.util.db.typehandler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jaqlib.util.db.TypeHandler;

/**
 * @author Werner Fragner
 */
public class ArrayTypeHandler implements TypeHandler
{

  public Object getObject(ResultSet resultSet, String columnLabel)
      throws SQLException
  {
    return resultSet.getArray(columnLabel);
  }


  public Object getObject(ResultSet resultSet, int columnIndex)
      throws SQLException
  {
    return resultSet.getArray(columnIndex);
  }

}
