package org.jaqlib.db.java.typehandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Werner Fragner
 */
public class NullJavaTypeHandler implements JavaTypeHandler
{

  public Object convert(Object value)
  {
    return value;
  }


  public void setObject(PreparedStatement stmt, int index, Object value)
      throws SQLException
  {
    stmt.setObject(index, value);
  }


  public Class<?>[] getSupportedTypes()
  {
    return new Class[] { Object.class };
  }

}
