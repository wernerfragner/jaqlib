package org.jaqlib.db.sql.typehandler;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Werner Fragner
 */
public class DateTypeHandler implements SqlTypeHandler
{

  public Object getObject(ResultSet resultSet, String columnLabel)
      throws SQLException
  {
    return resultSet.getDate(columnLabel);
  }


  public Object getObject(ResultSet resultSet, int columnIndex)
      throws SQLException
  {
    return resultSet.getDate(columnIndex);
  }

}
