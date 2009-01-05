package org.jaqlib;

import org.jaqlib.db.BeanMapping;
import org.jaqlib.db.DbInsertDataSource;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.Defaults;
import org.jaqlib.db.java.typehandler.CreditRatingTypeHandler;
import org.jaqlib.util.LogUtil;


public class BeanDatabaseQBInsertTest extends AbstractDatabaseQBTest
{

  private DbInsertDataSource dbInsertDataSource;
  private final String sql = "APP.ACCOUNT";


  @Override
  public void setUp() throws Exception
  {
    LogUtil.enableConsoleLogging();

    super.setUp();

    dbInsertDataSource = Database.getInsertDataSource(getDataSource(), sql);
  }


  public void testInsert_Simple()
  {
    Defaults.getJavaTypeHandlerRegistry().registerTypeHandler(
        CreditRating.class, new CreditRatingTypeHandler());

    AccountImpl account = new AccountImpl();
    account.setLastName("fragner");
    account.setFirstName("werner");
    account.setBalance(2000d);
    account.setCreditRating(CreditRating.GOOD);

    BeanMapping<AccountImpl> mapping = Database
        .getDefaultBeanMapping(AccountImpl.class);
    mapping.removeChildMapping("id");
    mapping.removeChildMapping("active");
    mapping.getChildColumn("lastName").setColumnName("lname");
    mapping.getChildColumn("firstName").setColumnName("fname");

    DatabaseQB.insert(account, mapping).into(dbInsertDataSource);

    String select = "SELECT id, lname AS lastname, fname AS firstname, creditrating AS creditrating, balance FROM APP.ACCOUNT";
    DbSelectDataSource dbSelectDataSource = Database.getSelectDataSource(
        getDataSource(), select);

    Account dummy = DatabaseQB.getRecorder(Account.class);
    AccountImpl dbAccount = DatabaseQB.select(AccountImpl.class).from(
        dbSelectDataSource).whereCall(dummy.getLastName()).isEqual("fragner")
        .uniqueResult();

    assertEqualAccount(account, dbAccount);
  }


  private void assertEqualAccount(AccountImpl expected, AccountImpl actual)
  {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getBalance(), actual.getBalance());
    assertEquals(expected.getCreditRating(), actual.getCreditRating());
  }

}
