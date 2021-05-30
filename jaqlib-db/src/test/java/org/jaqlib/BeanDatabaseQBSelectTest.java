package org.jaqlib;

import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.DbWhereClause;

import java.util.*;

import static org.jaqlib.AccountAssert.assertHuberAccount;
import static org.jaqlib.AccountAssert.assertMaierAccount;

public class BeanDatabaseQBSelectTest extends AbstractDatabaseQBTest
{

  private static final String HUBER = AccountSetup.HUBER_ACCOUNT.getLastName();
  private static final String MAIER = AccountSetup.MAIER_ACCOUNT.getLastName();

  private DbWhereClause<Account> where;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    String sql = "SELECT id, lname AS lastname, fname AS firstname, creditrating AS creditrating, balance FROM APP.ACCOUNT";
    Database db = new Database(getDataSource());
    DbSelectDataSource dataSource = db.getSelectDataSource(sql);
    BeanMapping<Account> mapping = db
        .getBeanMapping(AccountImpl.class);
    mapping.registerJavaTypeHandler(new CreditRatingTypeHandler());

    where = DatabaseQB.select(mapping).from(dataSource);
  }


  private void assertHashtableResult(DbWhereClause<Account> where)
  {
    Account account = DatabaseQB.getRecorder(Account.class);
    Hashtable<String, Account> accounts = where.asHashtable(account
        .getLastName());
    assertMapResult(accounts);
  }


  private void assertMapResult(DbWhereClause<Account> where)
  {
    Account account = DatabaseQB.getRecorder(Account.class);
    Map<String, Account> accounts = where.asMap(account.getLastName());
    assertMapResult(accounts);
  }


  private void assertMapResult(Map<String, Account> accounts)
  {
    org.jaqlib.AccountAssert.assertEquals(AccountSetup.MAIER_ACCOUNT, accounts
        .get(MAIER));
    org.jaqlib.AccountAssert.assertEquals(AccountSetup.HUBER_ACCOUNT, accounts
        .get(HUBER));
  }


  private void assertSetResult(DbWhereClause<Account> where)
  {
    Set<Account> accounts = where.asSet();
    assertEquals(2, accounts.size());
    List<Account> accountList = new ArrayList<Account>(accounts);
    assertListResult(accountList);
  }


  private void assertVectorResult(DbWhereClause<Account> where)
  {
    Vector<Account> accounts = where.asVector();
    assertListResult(accounts);
  }


  private void assertListResult(DbWhereClause<Account> where)
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
    Account account = where.whereCall(dummy.getBalance()).isGreaterThan(
        100000.0).uniqueResult();
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


  public void testSelect_PreparedStatement()
  {
    String sql = "SELECT id, lname AS lastname, fname AS firstname, creditrating AS creditrating, balance FROM APP.ACCOUNT WHERE lname = ?";
    DbSelectDataSource ds = Database.getSelectDataSource(getDataSource(), sql);
    ds.setAutoClosePreparedStatement(false);

    DbWhereClause<AccountImpl> where = DatabaseQB.select(AccountImpl.class)
        .from(ds);

    Account account = where.using(HUBER).firstResult();
    assertNotNull(account);
    assertHuberAccount(account);

    account = where.using(MAIER).firstResult();
    assertNotNull(account);
    assertMaierAccount(account);

    ds.close();
  }


  /**
   * Given WHERE clause produces one match.
   */
  public void testSelect_CustomSqlCondition_OneMatch()
  {
    Account account = where.where(DatabaseSetup.SELECT_SQL_WHERE)
        .uniqueResult();
    assertNotNull(account);
    assertHuberAccount(account);
  }


  /**
   * Given WHERE clause produces no match.
   */
  public void testSelect_CustomSqlCondition_NoMatch()
  {
    Account account = where.where("lname = 'invalid'").uniqueResult();
    assertNull(account);
  }


  /**
   * No SQL WHERE condition is specified, but a custom where condition class.
   */
  public void testSelect_CustomSqlCondition_Null()
  {
    WhereCondition<Account> condition = createWhereCondition(3500.0);
    Account account = where.where("").and(condition).uniqueResult();
    assertNotNull(account);
    assertHuberAccount(account);
  }

}
