package org.jaqlib.util.db.typehandler;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Werner Fragner
 */
public class BlobTypeHandler implements DbFieldTypeHandler
{

  public Object getObject(ResultSet resultSet, String columnLabel)
      throws SQLException
  {
    return resultSet.getBlob(columnLabel);
  }


  public Object getObject(ResultSet resultSet, int columnIndex)
      throws SQLException
  {
    return resultSet.getBlob(columnIndex);
  }

}
