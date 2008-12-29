package org.jaqlib;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.jaqlib.db.BeanDbSelectResult;
import org.jaqlib.db.DbSelect;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;


public class BeanDatabaseQBTest extends TestCase
{

  private static final String HUBER = DatabaseSetup.HUBER_ACCOUNT.getLastName();
  private static final String MAIER = DatabaseSetup.MAIER_ACCOUNT.getLastName();

  private DatabaseSetup dbSetup;
  private WhereClause<AccountImpl, DbSelect> where;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    disableLogging();

    dbSetup = new DatabaseSetup();
    dbSetup.createTestTables();
    dbSetup.insertTestRecords();

    final String sql = "SELECT lastname, firstname, creditrating, balance FROM APP.ACCOUNT";
    DbSelect dataSource = Db.getSelect(getDataSource(), sql);
    BeanDbSelectResult<AccountImpl> resultDefinition = Db
        .getBeanResult(AccountImpl.class);

    where = DatabaseQB.select(resultDefinition).from(dataSource);
  }


  private void disableLogging()
  {
    Logger logger = Logger.getLogger("org.jaqlib");
    logger.setLevel(Level.WARNING);
  }


  @Override
  public void tearDown() throws Exception
  {
    super.tearDown();

    dbSetup.clear();
  }


  private DataSource getDataSource()
  {
    return dbSetup.getDataSource();
  }


  public void testSelect_NoCondition()
  {
    assertListResult(where);
    assertVectorResult(where);
    assertSetResult(where);
    assertMapResult(where);
    assertHashtableResult(where);
  }


  private void assertHashtableResult(WhereClause<AccountImpl, DbSelect> where)
  {
    Account account = DatabaseQB.getMethodCallRecorder(Account.class);
    Hashtable<String, AccountImpl> accounts = where.asHashtable(account
        .getLastName());
    assertMapResult(accounts);
  }


  private void assertMapResult(WhereClause<AccountImpl, DbSelect> where)
  {
    Account account = DatabaseQB.getMethodCallRecorder(Account.class);
    Map<String, AccountImpl> accounts = where.asMap(account.getLastName());
    assertMapResult(accounts);
  }


  private void assertMapResult(Map<String, AccountImpl> accounts)
  {
    assertEquals(DatabaseSetup.MAIER_ACCOUNT, accounts.get(MAIER));
    assertEquals(DatabaseSetup.HUBER_ACCOUNT, accounts.get(HUBER));
  }


  private void assertSetResult(WhereClause<AccountImpl, DbSelect> where)
  {
    Set<AccountImpl> accounts = where.asSet();
    assertEquals(2, accounts.size());
    List<AccountImpl> accountList = new ArrayList<AccountImpl>(accounts);
    assertListResult(accountList);
  }


  private void assertVectorResult(WhereClause<AccountImpl, DbSelect> where)
  {
    Vector<AccountImpl> accounts = where.asVector();
    assertListResult(accounts);
  }


  private void assertListResult(WhereClause<AccountImpl, DbSelect> where)
  {
    List<AccountImpl> accounts = where.asList();
    assertListResult(accounts);
  }


  private void assertListResult(List<AccountImpl> accounts)
  {
    assertEquals(2, accounts.size());
    if (HUBER.equals(accounts.get(0).getLastName()))
    {
      assertHuberAccount(accounts.get(0));
      assertMaierAccount(accounts.get(1));
    }
    else if (MAIER.equals(accounts.get(0).getLastName()))
    {
      assertMaierAccount(accounts.get(0));
      assertHuberAccount(accounts.get(1));
    }
    else
    {
      fail("No matching account available!");
    }
  }


  private void assertMaierAccount(Account account)
  {
    assertEquals(DatabaseSetup.MAIER_ACCOUNT, account);
  }


  private void assertHuberAccount(Account account)
  {
    assertEquals(DatabaseSetup.HUBER_ACCOUNT, account);
  }


  private void assertEquals(Account expected, Account given)
  {
    assertEquals(expected.getLastName(), given.getLastName());
    assertEquals(expected.getFirstName(), given.getFirstName());
    assertEquals(expected.getBalance(), given.getBalance());
    assertEquals(expected.getCreditRating(), given.getCreditRating());
  }


  private WhereCondition<AccountImpl> createWhereCondition(final double balance)
  {
    return new WhereCondition<AccountImpl>()
    {

      public boolean evaluate(AccountImpl element)
      {
        return element.getBalance() > balance;
      }

    };
  }


  public void testSelect_MultipleFields_UserDefinedCondition_OneMatch()
  {
    WhereCondition<AccountImpl> condition = createWhereCondition(3500.0);
    Account account = where.where(condition).uniqueResult();
    assertNotNull(account);
    assertHuberAccount(account);
  }


  public void testSelect_MultipleFields_UserDefinedCondition_NoMatch()
  {
    WhereCondition<AccountImpl> condition = createWhereCondition(100000.0);
    Account account = where.where(condition).uniqueResult();
    assertNull(account);
  }


  public void testSelect_MultipleFields_MethodCallCondition_OneMatch()
  {
    Account dummy = DatabaseQB.getMethodCallRecorder(Account.class);
    Account account = where.where(dummy.getBalance()).isGreaterThan(3500.0)
        .uniqueResult();
    assertNotNull(account);
    assertHuberAccount(account);
  }


  public void testSelect_MultipleFields_MethodCallCondition_NoMatch()
  {
    Account dummy = DatabaseQB.getMethodCallRecorder(Account.class);
    Account account = where.where(dummy.getBalance()).isGreaterThan(100000.0)
        .uniqueResult();
    assertNull(account);
  }


  public void testSelect_MultipleFields_MixedConditions()
  {
    Account dummy = DatabaseQB.getMethodCallRecorder(Account.class);
    WhereCondition<AccountImpl> condition = createWhereCondition(3500.0);

    Account account = where.where(condition).andMethodCallResult(
        dummy.getCreditRating()).isEqual(CreditRating.GOOD).uniqueResult();
    assertNotNull(account);
    assertHuberAccount(account);
  }

}
