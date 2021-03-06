package org.jaqlib;

import org.easymock.EasyMock;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.db.*;
import org.jaqlib.db.util.DbUtil;
import org.jaqlib.util.db.DriverManagerDataSource;
import org.jaqlib.util.db.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class DatabaseSetup
{

  private SingleConnectionDataSource dataSource;


  public static final String ACCOUNT_TABLE = "APP.ACCOUNT";
  public static final String EXACT_ACCOUNT_TABLE = "APP.EXACT_ACCOUNT";
  public static final String SELECT_SQL = "SELET column FROM table";
  public static final String SELECT_SQL_WHERE = "lname LIKE 'h%'";

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
    ds.setUrl("jdbc:derby:memory:dbunittest;create=true");
    return new SingleConnectionDataSource(ds);
  }


  private Connection getConnection() throws SQLException
  {
    return getDataSource().getConnection();
  }


  public void dropTestTables()
  {
    try
    {
      executeStatement("DROP table " + ACCOUNT_TABLE);
      executeStatement("DROP table " + EXACT_ACCOUNT_TABLE);
    }
    catch (SQLException sqle)
    {
      // not relevant
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


  public void insertTestRecords()
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

    BeanMapping<AccountImpl> mapping = new BeanMapping<>(AccountImpl.class);
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
    EasyMock.expect(rs.getObject("id")).andReturn(1L);
    EasyMock.expect(rs.getObject("lastName")).andReturn(
            AccountSetup.HUBER_ACCOUNT.getLastName());
    EasyMock.expect(rs.getObject(1)).andReturn(1L);
    EasyMock.expect(rs.getObject(2)).andReturn(
            AccountSetup.HUBER_ACCOUNT.getLastName());

    EasyMock.expect(rs.next()).andReturn(true);
    EasyMock.expect(rs.getObject("id")).andReturn(2L);
    EasyMock.expect(rs.getObject("lastName")).andReturn(
            AccountSetup.MAIER_ACCOUNT.getLastName());
    EasyMock.expect(rs.getObject(1)).andReturn(2L);
    EasyMock.expect(rs.getObject(2)).andReturn(
            AccountSetup.MAIER_ACCOUNT.getLastName());

    EasyMock.expect(rs.next()).andReturn(false);

    EasyMock.replay(rs);
    EasyMock.replay(metaData);

    return rs;
  }


  public static DbResultSet getMockDbResultSet() throws SQLException
  {
    return new DbResultSet(getMockResultSet(),
        DbDefaults.INSTANCE.getSqlTypeHandlerRegistry(),
        DbDefaults.INSTANCE.getStrictFieldCheck());
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

    EasyMock.replay(ds);
    EasyMock.replay(conn);
    EasyMock.replay(stmt);
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

    EasyMock.replay(ds);
    EasyMock.replay(conn);
    EasyMock.replay(stmt);
    EasyMock.replay(prepStmt);
    return ds;
  }

}
