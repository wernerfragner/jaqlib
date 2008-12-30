package org.jaqlib.util.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Werner Fragner
 */
public interface TypeHandler
{

  Object getObject(ResultSet resultSet, String columnLabel) throws SQLException;


  Object getObject(ResultSet resultSet, int columnIndex) throws SQLException;

}
