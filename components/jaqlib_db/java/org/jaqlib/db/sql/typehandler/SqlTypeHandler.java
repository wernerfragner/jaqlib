package org.jaqlib.db.sql.typehandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Werner Fragner
 */
public interface SqlTypeHandler
{

  Object getObject(ResultSet resultSet, String columnLabel) throws SQLException;


  Object getObject(ResultSet resultSet, int columnIndex) throws SQLException;

}
