package org.jaqlib;

import java.sql.Connection;
import java.sql.SQLException;

import org.jaqlib.util.db.DbUtil;
import org.jaqlib.util.db.DriverManagerDataSource;
import org.jaqlib.util.db.SingleConnectionDataSource;

public class DatabaseSetup
{

  private SingleConnectionDataSource dataSource;


  public static final AccountImpl HUBER_ACCOUNT;
  public static final AccountImpl MAIER_ACCOUNT;

  public static final AccountImpl[] ACCOUNTS;

  static
  {
    HUBER_ACCOUNT = new AccountImpl();
    HUBER_ACCOUNT.setLastName("huber");
    HUBER_ACCOUNT.setFirstName("sepp");
    HUBER_ACCOUNT.setBalance(5000.0);
    HUBER_ACCOUNT.setCreditRating(CreditRating.GOOD);

    MAIER_ACCOUNT = new AccountImpl();
    MAIER_ACCOUNT.setLastName("maier");
    MAIER_ACCOUNT.setFirstName("franz");
    MAIER_ACCOUNT.setBalance(2000.0);
    MAIER_ACCOUNT.setCreditRating(CreditRating.POOR);

    ACCOUNTS = new AccountImpl[] { HUBER_ACCOUNT, MAIER_ACCOUNT };
  }


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
        + "(START WITH 1, INCREMENT BY 1)," + "LNAME    VARCHAR(30), "
        + "FNAME   VARCHAR(30)," + "CREDITRATING INTEGER, BALANCE DOUBLE)";

    executeStatement(createAccount);
  }


  public void insertTestRecords() throws SQLException
  {
    for (AccountImpl account : ACCOUNTS)
    {
      executeStatement("INSERT INTO APP.ACCOUNT (lname, fname, creditrating, balance) VALUES ('"
          + account.getLastName()
          + "', '"
          + account.getFirstName()
          + "', "
          + account.getCreditRating().intValue()
          + ", "
          + account.getBalance()
          + ")");
    }
  }


  private void executeStatement(String sql) throws SQLException
  {
    DbUtil.executeStatement(getConnection(), sql);
  }


}
