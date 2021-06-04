package org.jaqlib;

import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.db.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeanDatabaseQBDmlTest extends AbstractDatabaseQBTest
{

  private static final String TABLE = DatabaseSetup.ACCOUNT_TABLE;
  private static final String EXACT_TABLE = DatabaseSetup.EXACT_ACCOUNT_TABLE;
  private static final String SELECT = DatabaseSetup.ACCOUNT_SELECT;
  private static final String EXACT_SELECT = DatabaseSetup.EXACT_ACCOUNT_SELECT;

  private AccountImpl account;
  private BeanMapping<AccountImpl> mapping;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    DbDefaults.INSTANCE.registerJavaTypeHandler(new CreditRatingTypeHandler());
    DbDefaults.INSTANCE.registerJavaTypeHandler(new BooleanTypeHandler());

    account = createAccount();

    mapping = Database.getDefaultBeanMapping(AccountImpl.class);
    mapping.removeField("id");
    mapping.removeField("active");
    mapping.removeField("department");
    mapping.removeField("lastName");
    mapping.removeField("firstName");
    mapping.removeField("transactions");

    ColumnMapping<?> lname = new ColumnMapping<>("lastName");
    lname.setColumnName("lname");
    mapping.addField(lname);

    ColumnMapping<?> fname = new ColumnMapping<>("firstName");
    fname.setColumnName("fname");
    mapping.addField(fname);
  }


  private void registerManualMappingStrategy()
  {
    BeanMapping<Account> mapping2 = new BeanMapping<>(AccountImpl.class);
    mapping2.removeField("transactions");

    ManualMappingStrategy strategy = new ManualMappingStrategy();
    strategy.addMappings(mapping2.getFieldMappings());
    DbDefaults.INSTANCE.setBeanMappingStrategy(strategy);
  }


  @Override
  public void tearDown() throws Exception
  {
    super.tearDown();

    DbDefaults.INSTANCE.reset();
  }


  private DbInsertDataSource getInsertDataSource(String tableName)
  {
    return Database.getInsertDataSource(getDataSource(), tableName);
  }


  private DbUpdateDataSource getUpdateDataSource(Number id, String table)
  {
    String whereClause = "id = " + id;
    return Database.getUpdateDataSource(getDataSource(), table, whereClause);
  }


  private DbDeleteDataSource getDeleteDataSource(String table)
  {
    return Database.getDeleteDataSource(getDataSource(), table);
  }


  private AccountImpl selectAccount(String select)
  {
    DbSelectDataSource dbSelectDataSource = getSelectDataSource(select);

    Account dummy = DatabaseQB.getRecorder(Account.class);
    AccountImpl dbAccount = DatabaseQB.select(AccountImpl.class)
        .from(dbSelectDataSource).whereCall(dummy.getLastName())
        .isEqual("fragner").uniqueResult();
    assertEqualAccount(account, dbAccount);
    return dbAccount;
  }


  private DbSelectDataSource getSelectDataSource(String select)
  {
    return Database.getSelectDataSource(getDataSource(), select);
  }


  private void assertEqualAccount(AccountImpl expected, AccountImpl actual)
  {
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getBalance(), actual.getBalance());
    assertEquals(expected.getCreditRating(), actual.getCreditRating());
  }


  private AccountImpl createAccount()
  {
    AccountImpl account = new AccountImpl();
    account.setLastName("fragner");
    account.setFirstName("werner");
    account.setBalance(2000d);
    account.setCreditRating(CreditRating.GOOD);
    return account;
  }


  public void testInsert_CustomMapping()
  {
    DbInsertDataSource ds = getInsertDataSource(TABLE);
    int cnt = DatabaseQB.insert(account).into(ds).using(mapping);
    assertEquals(1, cnt);

    AccountImpl dbAccount = selectAccount(SELECT);
    assertEqualAccount(account, dbAccount);
  }


  public void testInsert_DefaultMapping_NullValue()
  {
    account.setFirstName(null);

    DbInsertDataSource ds = getInsertDataSource(TABLE);
    int cnt = DatabaseQB.insert(account).into(ds).using(mapping);
    assertEquals(1, cnt);

    AccountImpl dbAccount = selectAccount(SELECT);
    assertEqualAccount(account, dbAccount);
  }


  public void testInsert_DefaultMapping()
  {
    // is needed because default mapping is used (a 'transactions' table is not
    // available at database)
    registerManualMappingStrategy();

    int cnt = DatabaseQB.insert(account).into(getInsertDataSource(EXACT_TABLE))
        .usingDefaultMapping();
    assertEquals(1, cnt);

    AccountImpl dbAccount = selectAccount(EXACT_SELECT);
    assertEqualAccount(account, dbAccount);
  }


  public void testUpdate_CustomMapping()
  {
    // insert account

    dbSetup.insertAccount(account);
    AccountImpl insertedAccount = selectAccount(SELECT);

    // update account

    DbUpdateDataSource ds = getUpdateDataSource(insertedAccount.getId(), TABLE);
    account.setBalance(3000d);
    int cnt = DatabaseQB.update(account).in(ds).using(mapping);
    assertEquals(1, cnt);

    // check if account has been updated at database

    AccountImpl dbAccount = selectAccount(SELECT);
    assertEqualAccount(account, dbAccount);
  }


  public void testUpdate_DefaultMapping()
  {
    // is needed because default mapping is used (a 'transactions' table is not
    // available at database)
    registerManualMappingStrategy();

    // insert account

    dbSetup.insertExactAccount(account);
    AccountImpl insertedAccount = selectAccount(EXACT_SELECT);

    // update account

    DbUpdateDataSource ds = getUpdateDataSource(insertedAccount.getId(),
        EXACT_TABLE);
    account.setBalance(3000d);
    int cnt = DatabaseQB.update(account).in(ds).usingDefaultMapping();
    assertEquals(1, cnt);

    // check if account has been updated at database

    AccountImpl dbAccount = selectAccount(EXACT_SELECT);
    assertEqualAccount(account, dbAccount);
  }


  public void testUpdate_MatchingWhereClause()
  {
    // is needed because default mapping is used (a 'transactions' table is not
    // available at database)
    registerManualMappingStrategy();

    // insert account

    dbSetup.insertExactAccount(account);
    AccountImpl insertedAccount = selectAccount(EXACT_SELECT);

    // update account with matching where clause

    DbUpdateDataSource ds = getUpdateDataSource(insertedAccount.getId(),
        EXACT_TABLE);
    String whereClause = "lastname = '" + account.getLastName() + "'";
    account.setBalance(3000d);
    int cnt = DatabaseQB.update(account).in(ds).where(whereClause)
        .usingDefaultMapping();
    assertEquals(1, cnt);

    // check if account has been updated at database

    AccountImpl dbAccount = selectAccount(EXACT_SELECT);
    assertEqualAccount(account, dbAccount);
  }


  public void testUpdate_NonMatchingWhereClause()
  {
    // is needed because default mapping is used (a 'transactions' table is not
    // available at database)
    registerManualMappingStrategy();

    double originalBalance = account.getBalance();

    // insert account

    dbSetup.insertExactAccount(account);
    AccountImpl insertedAccount = selectAccount(EXACT_SELECT);

    // update account with non-matching where clause

    DbUpdateDataSource ds = getUpdateDataSource(insertedAccount.getId(),
        EXACT_TABLE);
    String whereClause = "lastname = 'nonmatching'";
    account.setBalance(originalBalance + 1000);
    int cnt = DatabaseQB.update(account).in(ds).where(whereClause)
        .usingDefaultMapping();
    assertEquals(0, cnt);
    // reset for compare operation
    account.setBalance(originalBalance);

    // check if account has been updated at database

    AccountImpl dbAccount = selectAccount(EXACT_SELECT);
    assertEqualAccount(account, dbAccount);
  }


  public void testDelete()
  {
    assertEquals(2, getNrRecords(TABLE));

    DbDeleteDataSource ds = getDeleteDataSource(TABLE);
    int cnt = DatabaseQB.delete().from(ds);
    assertEquals(2, cnt);

    assertEquals(0, getNrRecords(TABLE));
  }


  private int getNrRecords(String table)
  {
    return dbSetup.getNrRecords(table);
  }

}
