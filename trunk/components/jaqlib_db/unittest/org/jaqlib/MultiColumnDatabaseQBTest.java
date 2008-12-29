package org.jaqlib;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.jaqlib.db.DbSelect;
import org.jaqlib.db.DbSelectResult;
import org.jaqlib.query.WhereClause;


public class MultiColumnDatabaseQBTest extends TestCase
{

  private DatabaseSetup dbSetup;
  private WhereClause<AccountImpl, DbSelect> where;


  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    dbSetup = new DatabaseSetup();
    dbSetup.createTestTables();
    dbSetup.insertTestRecords();

    final String sql = "";
    DbSelect dataSource = Db.getSelect(getDataSource(), sql);
    DbSelectResult<AccountImpl> resultDefinition = Db
        .getComplexResult(AccountImpl.class);

    where = DatabaseQB.select(resultDefinition).from(dataSource);
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


  private void assertHashtableResult(WhereClause<AccountImpl, DbSelect> where2)
  {
    // TODO Auto-generated method stub

  }


  private void assertMapResult(WhereClause<AccountImpl, DbSelect> where2)
  {
    // TODO Auto-generated method stub

  }


  private void assertSetResult(WhereClause<AccountImpl, DbSelect> where2)
  {
    // TODO Auto-generated method stub

  }


  private void assertVectorResult(WhereClause<AccountImpl, DbSelect> where2)
  {
    // TODO Auto-generated method stub

  }


  private void assertListResult(WhereClause<AccountImpl, DbSelect> where2)
  {
    // TODO Auto-generated method stub

  }


  public void testSelect_MultipleFields()
  {
  }


  public void testSelect_MultipleFields_UserDefinedCondition()
  {
  }


  public void testSelect_MultipleFields_MethodCallCondition()
  {
  }

}
