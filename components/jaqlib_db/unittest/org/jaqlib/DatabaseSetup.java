package org.jaqlib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.easymock.EasyMock;
import org.jaqlib.db.BeanMapping;
import org.jaqlib.db.DbInsertDataSource;
import org.jaqlib.db.DbResultSet;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.Defaults;
import org.jaqlib.db.ManualMappingStrategy;
import org.jaqlib.db.java.typehandler.CreditRatingTypeHandler;
import org.jaqlib.util.DbUtil;
import org.jaqlib.util.db.DriverManagerDataSource;
import org.jaqlib.util.db.SingleConnectionDataSource;

public class DatabaseSetup
{

  private SingleConnectionDataSource dataSource;


  public static final String ACCOUNT_TABLE = "APP.ACCOUNT";
  public static final String EXACT_ACCOUNT_TABLE = "APP.EXACT_ACCOUNT";
  public static final String SELECT_SQL = "SELET column FROM table";

  public static final String ACCOUNT_SELECT = "SELECT id, lname AS lastname, fname AS firstname, creditrating AS creditrating, balance FROM "
      + ACCOUNT_TABLE;
  public static final String EXACT_ACCOUNT_SELECT = "SELECT id, lastname, firstname, creditrating AS creditrating, balance, active, department FROM "
      + EXACT_ACCOUNT_TABLE;


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
      executeStatement("DROP table " + ACCOUNT_TABLE);
      executeStatement("DROP table " + EXACT_ACCOUNT_TABLE);
    }
    catch (SQLException sqle)
    {
    }
  }


  public void createTestTables() throws SQLException
  {
    dropTestTables();

    String createAccount = "CREATE table " + ACCOUNT_TABLE + " ("
        + "ID          INTEGER NOT NULL "
        + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
        + "(START WITH 1, INCREMENT BY 1)," + "LNAME    VARCHAR(30), "
        + "FNAME   VARCHAR(30)," + "CREDITRATING INTEGER, BALANCE DOUBLE)";

    String createExactAccount = "CREATE table "
        + EXACT_ACCOUNT_TABLE
        + " ("
        + "ID INTEGER, LASTNAME VARCHAR(30), FIRSTNAME VARCHAR(30),"
        + "CREDITRATING INTEGER, BALANCE DOUBLE, ACTIVE INTEGER, DEPARTMENT VARCHAR(100))";

    executeStatement(createAccount);
    executeStatement(createExactAccount);
  }


  public void insertTestRecords() throws SQLException
  {
    DbInsertDataSource ds = createAccountInsertDataSource(ACCOUNT_TABLE);
    BeanMapping<AccountImpl> mapping = createAccountMapping();

    for (AccountImpl account : AccountSetup.ACCOUNTS)
    {
      insertAccount(ds, mapping, account);
    }
  }


  private DbInsertDataSource createAccountInsertDataSource(String table)
  {
    return Database.getInsertDataSource(getDataSource(), table);
  }


  private BeanMapping<AccountImpl> createAccountMapping()
  {
    ManualMappingStrategy strategy = new ManualMappingStrategy();
    strategy.addColumnMapping("lName", "lastName");
    strategy.addColumnMapping("fName", "firstName");
    strategy.addColumnMapping("balance");
    strategy.addColumnMapping("creditRating");

    BeanMapping<AccountImpl> mapping = new BeanMapping<AccountImpl>(
        AccountImpl.class);
    mapping.setMappingStrategy(strategy);
    mapping.registerJavaTypeHandler(new CreditRatingTypeHandler());
    return mapping;
  }


  private void insertAccount(DbInsertDataSource ds,
      BeanMapping<AccountImpl> mapping, AccountImpl account)
  {
    DatabaseQB.insert(account).into(ds).using(mapping);
  }


  public void insertAccount(AccountImpl account)
  {
    DbInsertDataSource ds = createAccountInsertDataSource(ACCOUNT_TABLE);
    BeanMapping<AccountImpl> mapping = createAccountMapping();

    insertAccount(ds, mapping, account);
  }


  public void insertExactAccount(AccountImpl account)
  {
    DbInsertDataSource ds = createAccountInsertDataSource(EXACT_ACCOUNT_TABLE);
    BeanMapping<AccountImpl> mapping = Database.getDefaultBeanMapping(account
        .getClass());

    insertAccount(ds, mapping, account);
  }


  public Integer getNrRecords(String table)
  {
    DbSelectDataSource ds = Database.getSelectDataSource(getDataSource(),
        "select count(*) as nrRecords from " + table);
    return (Integer) DatabaseQB.select("nrRecords").from(ds).uniqueResult();
  }


  private void executeStatement(String sql) throws SQLException
  {
    DbUtil.executeStatement(getConnection(), sql);
  }


  public static ResultSet getMockResultSet() throws SQLException
  {
    // id and lastname columns are available in ResultSet

    ResultSet rs = EasyMock.createNiceMock(ResultSet.class);
    ResultSetMetaData metaData = EasyMock
        .createNiceMock(ResultSetMetaData.class);
    EasyMock.expect(rs.getMetaData()).andReturn(metaData).anyTimes();
    EasyMock.expect(metaData.getColumnCount()).andReturn(2);
    EasyMock.expect(metaData.getColumnLabel(1)).andReturn("id");
    EasyMock.expect(metaData.getColumnLabel(2)).andReturn("lastname");

    EasyMock.expect(rs.next()).andReturn(true);
    EasyMock.expect(rs.getObject("id")).andReturn(Long.valueOf(1));
    EasyMock.expect(rs.getObject("lastName")).andReturn(
        AccountSetup.HUBER_ACCOUNT.getLastName());
    EasyMock.expect(rs.getObject(1)).andReturn(Long.valueOf(1));
    EasyMock.expect(rs.getObject(2)).andReturn(
        AccountSetup.HUBER_ACCOUNT.getLastName());

    EasyMock.expect(rs.next()).andReturn(true);
    EasyMock.expect(rs.getObject("id")).andReturn(Long.valueOf(2));
    EasyMock.expect(rs.getObject("lastName")).andReturn(
        AccountSetup.MAIER_ACCOUNT.getLastName());
    EasyMock.expect(rs.getObject(1)).andReturn(Long.valueOf(2));
    EasyMock.expect(rs.getObject(2)).andReturn(
        AccountSetup.MAIER_ACCOUNT.getLastName());

    EasyMock.expect(rs.next()).andReturn(false);

    EasyMock.replay(rs, metaData);

    return rs;
  }


  public static DbResultSet getMockDbResultSet() throws SQLException
  {
    return new DbResultSet(getMockResultSet(), Defaults
        .getSqlTypeHandlerRegistry(), Defaults.getStrictColumnCheck());
  }


  public static DataSource getStrictMockDataSource(String sql)
      throws SQLException
  {
    ResultSet resultSet = getMockResultSet();
    Statement stmt = EasyMock.createNiceMock(Statement.class);
    Connection conn = EasyMock.createNiceMock(Connection.class);
    DataSource ds = EasyMock.createNiceMock(DataSource.class);

    EasyMock.expect(ds.getConnection()).andReturn(conn).once();
    EasyMock.expect(conn.createStatement()).andReturn(stmt).once();
    EasyMock.expect(stmt.executeQuery(sql)).andReturn(resultSet).once();

    EasyMock.replay(ds, conn, stmt);
    return ds;
  }


  public static DataSource getNiceMockDataSource() throws SQLException
  {
    ResultSet resultSet = getMockResultSet();
    Statement stmt = EasyMock.createNiceMock(Statement.class);
    PreparedStatement prepStmt = EasyMock
        .createNiceMock(PreparedStatement.class);
    Connection conn = EasyMock.createNiceMock(Connection.class);
    DataSource ds = EasyMock.createNiceMock(DataSource.class);

    EasyMock.expect(ds.getConnection()).andReturn(conn);
    EasyMock.expect(conn.createStatement()).andReturn(stmt);
    EasyMock.expect(conn.prepareStatement(SELECT_SQL)).andReturn(prepStmt);
    EasyMock.expect(stmt.executeQuery(SELECT_SQL)).andReturn(resultSet)
        .anyTimes();
    EasyMock.expect(prepStmt.executeQuery()).andReturn(resultSet).anyTimes();

    EasyMock.replay(ds, conn, stmt, prepStmt);
    return ds;
  }


  public static DbSelectDataSource getDbSelectDataSource() throws SQLException
  {
    return new DbSelectDataSource(getNiceMockDataSource(), SELECT_SQL);
  }


}
