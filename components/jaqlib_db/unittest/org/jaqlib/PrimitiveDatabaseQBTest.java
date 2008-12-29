package org.jaqlib;

import static org.jaqlib.DatabaseSetup.HUBER_ACCOUNT;
import static org.jaqlib.DatabaseSetup.MAIER_ACCOUNT;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.jaqlib.db.DbSelect;
import org.jaqlib.db.PrimitiveDbSelectResult;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;


public class PrimitiveDatabaseQBTest extends TestCase
{

  private DatabaseSetup dbSetup;

  private WhereClause<String, DbSelect> where;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    dbSetup = new DatabaseSetup();
    dbSetup.createTestTables();
    dbSetup.insertTestRecords();

    final String sql = "SELECT lastname FROM APP.ACCOUNT";
    DbSelect selectDefinition = Db.getSelect(getDataSource(), sql);
    PrimitiveDbSelectResult<String> resultDefinition = Db.getPrimitiveResult(1);

    where = DatabaseQB.select(resultDefinition).from(selectDefinition);
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


  private void assertLastResult(WhereClause<String, DbSelect> where)
  {
    assertEquals(MAIER_ACCOUNT.getLastName(), where.lastResult());
  }


  private void assertFirstResult(WhereClause<String, DbSelect> where)
  {
    assertEquals(HUBER_ACCOUNT.getLastName(), where.firstResult());
  }


  private void assertSetResult(WhereClause<String, DbSelect> where)
  {
    Set<String> result = where.asSet();
    assertAllLastNames(result);
  }


  private void assertVectorResult(WhereClause<String, DbSelect> where)
  {
    Vector<String> result = where.asVector();
    assertAllLastNames(result);
  }


  private void assertListResult(WhereClause<String, DbSelect> where)
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


  public void testSelect_SimpleCondition()
  {
    String huber = HUBER_ACCOUNT.getLastName();

    String result = where.where().element().isEqual(huber).uniqueResult();
    assertEquals(huber, result);
  }


  public void testSelect_MultipleSimpleConditions()
  {
    String maier = MAIER_ACCOUNT.getLastName();
    String huber = HUBER_ACCOUNT.getLastName();

    List<String> result = where.where().element().isEqual(huber).or().element()
        .isEqual(maier).asList();
    assertAllLastNames(result);
  }


  public void testSelect_UserDefinedCondition()
  {
    String huber = HUBER_ACCOUNT.getLastName();

    WhereCondition<String> condition = createWhereCondition("h.*");
    assertEquals(huber, where.where(condition).uniqueResult());
  }


  public void testSelect_MultipleUserDefinedConditions()
  {
    WhereCondition<String> condition1 = createWhereCondition("h.*");
    WhereCondition<String> condition2 = createWhereCondition(".*ier");

    List<String> result = where.where(condition1).or(condition2).asList();
    assertAllLastNames(result);
  }


}
