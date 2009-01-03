package org.jaqlib;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.jaqlib.core.WhereClause;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.db.BeanMapping;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.java.typehandler.CreditRatingTypeHandler;


public class BeanDatabaseQBTest extends TestCase
{

  private static final String HUBER = DatabaseSetup.HUBER_ACCOUNT.getLastName();
  private static final String MAIER = DatabaseSetup.MAIER_ACCOUNT.getLastName();

  private DatabaseSetup dbSetup;
  private WhereClause<Account, DbSelectDataSource> where;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    dbSetup = new DatabaseSetup();
    dbSetup.createTestTables();
    dbSetup.insertTestRecords();

    String sql = "SELECT lname AS lastname, fname AS firstname, creditrating AS creditrating, balance FROM APP.ACCOUNT";
    Database db = new Database(getDataSource());
    DbSelectDataSource dataSource = db.getSelectDataSource(sql);
    BeanMapping<Account> mapping = db
        .<Account> getBeanMapping(AccountImpl.class);
    mapping.registerJavaTypeHandler(CreditRating.class,
        new CreditRatingTypeHandler());

    where = DatabaseQB.select(mapping).from(dataSource);
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


  private void assertHashtableResult(
      WhereClause<Account, DbSelectDataSource> where)
  {
    Account account = DatabaseQB.getRecorder(Account.class);
    Hashtable<String, Account> accounts = where.asHashtable(account
        .getLastName());
    assertMapResult(accounts);
  }


  private void assertMapResult(WhereClause<Account, DbSelectDataSource> where)
  {
    Account account = DatabaseQB.getRecorder(Account.class);
    Map<String, Account> accounts = where.asMap(account.getLastName());
    assertMapResult(accounts);
  }


  private void assertMapResult(Map<String, Account> accounts)
  {
    assertEquals(DatabaseSetup.MAIER_ACCOUNT, accounts.get(MAIER));
    assertEquals(DatabaseSetup.HUBER_ACCOUNT, accounts.get(HUBER));
  }


  private void assertSetResult(WhereClause<Account, DbSelectDataSource> where)
  {
    Set<Account> accounts = where.asSet();
    assertEquals(2, accounts.size());
    List<Account> accountList = new ArrayList<Account>(accounts);
    assertListResult(accountList);
  }


  private void assertVectorResult(WhereClause<Account, DbSelectDataSource> where)
  {
    Vector<Account> accounts = where.asVector();
    assertListResult(accounts);
  }


  private void assertListResult(WhereClause<Account, DbSelectDataSource> where)
  {
    List<Account> accounts = where.asList();
    assertListResult(accounts);
  }


  private void assertListResult(List<Account> accounts)
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


  private WhereCondition<Account> createWhereCondition(final double balance)
  {
    return new WhereCondition<Account>()
    {

      public boolean evaluate(Account element)
      {
        return element.getBalance() > balance;
      }

    };
  }


  public void testSelect_NoCondition()
  {
    assertListResult(where);
    assertVectorResult(where);
    assertSetResult(where);
    assertMapResult(where);
    assertHashtableResult(where);
  }


  public void testSelect_CustomCondition_OneMatch()
  {
    WhereCondition<Account> condition = createWhereCondition(3500.0);
    Account account = where.where(condition).uniqueResult();
    assertNotNull(account);
    assertHuberAccount(account);
  }


  public void testSelect_CustomCondition_NoMatch()
  {
    WhereCondition<Account> condition = createWhereCondition(100000.0);
    Account account = where.where(condition).uniqueResult();
    assertNull(account);
  }


  public void testSelect_MethodCallCondition_OneMatch()
  {
    Account dummy = DatabaseQB.getRecorder(Account.class);
    Account account = where.whereCall(dummy.getBalance()).isGreaterThan(3500.0)
        .uniqueResult();
    assertNotNull(account);
    assertHuberAccount(account);
  }


  public void testSelect_MethodCallCondition_NoMatch()
  {
    Account dummy = DatabaseQB.getRecorder(Account.class);
    Account account = where.whereCall(dummy.getBalance()).isGreaterThan(100000.0)
        .uniqueResult();
    assertNull(account);
  }


  public void testSelect_MixedConditions()
  {
    Account dummy = DatabaseQB.getRecorder(Account.class);
    WhereCondition<Account> condition = createWhereCondition(3500.0);

    Account account = where.where(condition).andCall(dummy.getCreditRating())
        .isEqual(CreditRating.GOOD).uniqueResult();
    assertNotNull(account);
    assertHuberAccount(account);
  }

}
