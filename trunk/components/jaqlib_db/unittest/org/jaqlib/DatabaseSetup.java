package org.jaqlib;

import java.sql.Connection;
import java.sql.SQLException;

import org.jaqlib.db.DbUtil;
import org.jaqlib.db.DriverManagerDataSource;
import org.jaqlib.db.SingleConnectionDataSource;

public class DatabaseSetup
{

  private SingleConnectionDataSource dataSource;


  public void clear() throws SQLException
  {
    if (dataSource != null)
    {
      dropTestTables();
      dataSource.close();
      dataSource = null;
    }
  }


  public SingleConnectionDataSource getDataSource()
  {
    if (dataSource == null)
    {
      dataSource = createDataSource();
    }
    return dataSource;
  }


  private SingleConnectionDataSource createDataSource()
  {
    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
    ds.setUrl("jdbc:derby:dbunittest;create=true");
    return new SingleConnectionDataSource(ds);
  }


  private Connection getConnection() throws SQLException
  {
    return getDataSource().getConnection();
  }


  public void dropTestTables() throws SQLException
  {
    try
    {
      executeStatement("DROP table APP.ACCOUNT");
    }
    catch (SQLException sqle)
    {
    }
  }


  public void createTestTables() throws SQLException
  {
    dropTestTables();

    String createAccount = "CREATE table APP.ACCOUNT ("
        + "ID          INTEGER NOT NULL "
        + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
        + "(START WITH 1, INCREMENT BY 1)," + "LASTNAME    VARCHAR(30), "
        + "FIRSTNAME   VARCHAR(30)," + "CREDITRATING INTEGER, BALANCE DOUBLE)";

    executeStatement(createAccount);
  }


  public void insertTestRecords() throws SQLException
  {
    executeStatement("INSERT INTO APP.ACCOUNT (lastname, firstname, phone, email, creditrating) VALUES ('bauer', 'hans', '3983', 'hans.bauer@email.com', 1)");
    executeStatement("INSERT INTO APP.ACCOUNT (lastname, firstname, phone, email, creditrating) VALUES ('wurm', 'sepp', '222', 'sepp.wurm@email.com', 7)");
  }


  private void executeStatement(String sql) throws SQLException
  {
    DbUtil.executeStatement(getConnection(), sql);
  }


}
