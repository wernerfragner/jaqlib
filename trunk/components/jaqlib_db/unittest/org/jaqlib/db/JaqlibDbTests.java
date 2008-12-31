package org.jaqlib.db;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JaqlibDbTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib.db");
    //$JUnit-BEGIN$
    suite.addTestSuite(BeanConventionMappingStrategyTest.class);
    suite.addTestSuite(BeanMappingTest.class);
    suite.addTestSuite(ColumnMappingTest.class);
    suite.addTestSuite(DbResultSetTest.class);
    suite.addTestSuite(DbResultSetMetaDataTest.class);
    //$JUnit-END$
    return suite;
  }

}
