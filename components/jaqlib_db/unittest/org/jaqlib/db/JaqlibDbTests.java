package org.jaqlib.db;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jaqlib.db.sql.typehandler.DefaultSqlTypeHandlerRegistryTest;

public class JaqlibDbTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib.db");
    // $JUnit-BEGIN$
    suite.addTestSuite(DbSelectDataSourceTest.class);
    suite.addTestSuite(ColumnMappingTest.class);
    suite.addTestSuite(DbResultSetTest.class);
    suite.addTestSuite(DbResultSetMetaDataTest.class);
    suite.addTestSuite(DefaultSqlTypeHandlerRegistryTest.class);
    // $JUnit-END$
    return suite;
  }

}
