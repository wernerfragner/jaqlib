package org.jaqlib;

import static org.jaqlib.AccountSetup.HUBER_ACCOUNT;
import static org.jaqlib.AccountSetup.MAIER_ACCOUNT;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.ColumnMapping;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.DbWhereClause;


public class ColumnDatabaseQBSelectTest extends TestCase
{

  private DatabaseSetup dbSetup;

  private DbWhereClause<String> where;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    dbSetup = new DatabaseSetup();
    dbSetup.createTestTables();
    dbSetup.insertTestRecords();

    final String sql = "SELECT lname AS lastname FROM APP.ACCOUNT";
    DbSelectDataSource dataSource = Database.getSelectDataSource(
        getDataSource(), sql);

    where = DatabaseQB.select(new ColumnMapping<String>(1)).from(dataSource);
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


  private void assertLastResult(DbWhereClause<String> where)
  {
    assertEquals(MAIER_ACCOUNT.getLastName(), where.lastResult());
  }


  private void assertFirstResult(DbWhereClause<String> where)
  {
    assertEquals(HUBER_ACCOUNT.getLastName(), where.firstResult());
  }


  private void assertSetResult(DbWhereClause<String> where)
  {
    Set<String> result = where.asSet();
    assertAllLastNames(result);
  }


  private void assertVectorResult(DbWhereClause<String> where)
  {
    Vector<String> result = where.asVector();
    assertAllLastNames(result);
  }


  private void assertListResult(DbWhereClause<String> where)
  {
    List<String> result = where.asList();
    assertAllLastNames(result);
  }


  private void assertAllLastNames(Collection<String> result)
  {
    assertEquals(2, result.size());
    assertTrue(result.contains(HUBER_ACCOUNT.getLastName()));
    assertTrue(result.contains(MAIER_ACCOUNT.getLastName()));
  }


  private WhereCondition<String> createWhereCondition(final String pattern)
  {
    return new WhereCondition<String>()
    {

      public boolean evaluate(String element)
      {
        return element.matches(pattern);
      }
    };
  }


  public void testSelect()
  {
    assertListResult(where);
    assertVectorResult(where);
    assertSetResult(where);
    assertFirstResult(where);
    assertLastResult(where);
  }


  public void testSelect_SimpleCondition() throws Exception
  {
    String huber = HUBER_ACCOUNT.getLastName();

    String result = where.where().element().isEqual(huber).uniqueResult();
    assertEquals(huber, result);

    // test shortcut method

    result = where.whereElement().isEqual(huber).uniqueResult();
    assertEquals(huber, result);

    // test if connection has been closed

    assertFalse(getDataSource().getConnection().isClosed());
  }


  public void testSelect_MultipleSimpleConditions()
  {
    String maier = MAIER_ACCOUNT.getLastName();
    String huber = HUBER_ACCOUNT.getLastName();

    List<String> result = where.where().element().isEqual(huber).or().element()
        .isEqual(maier).asList();
    assertAllLastNames(result);

    // test shortcut method

    result = where.where().element().isEqual(huber).orElement().isEqual(maier)
        .asList();
    assertAllLastNames(result);
  }


  public void testSelect_CustomCondition()
  {
    String huber = HUBER_ACCOUNT.getLastName();

    WhereCondition<String> condition = createWhereCondition("h.*");
    assertEquals(huber, where.where(condition).uniqueResult());
  }


  public void testSelect_MultipleCustomConditions()
  {
    WhereCondition<String> condition1 = createWhereCondition("h.*");
    WhereCondition<String> condition2 = createWhereCondition(".*ier");

    List<String> result = where.where(condition1).or(condition2).asList();
    assertAllLastNames(result);
  }


  /**
   * SELECT statement must only be executed once when using the WhereClause for
   * different conditions with different results.
   */
  public void testSelect_CacheResultSet() throws SQLException
  {
    final String sql = "SELECT lname AS lastname FROM APP.ACCOUNT";
    DbSelectDataSource dataSource = Database.getSelectDataSource(
        getStrictMockDataSource(sql), sql);

    Account dummy = DatabaseQB.getRecorder(Account.class);
    DbWhereClause<AccountImpl> where = DatabaseQB.select(AccountImpl.class)
        .from(dataSource);

    assertNotNull(where.asList());
    assertNotNull(where.asVector());
    assertNotNull(where.asMap(dummy.getLastName()));
    assertNotNull(where.asHashtable(dummy.getLastName()));
  }


  private DataSource getStrictMockDataSource(String sql) throws SQLException
  {
    return DatabaseSetup.getStrictMockDataSource(sql);
  }


}
