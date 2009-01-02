package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jaqlib.db.JaqlibDbTests;
import org.jaqlib.util.LogUtil;

public class DatabaseQBTests
{

  public static Test suite()
  {
    LogUtil.disableJaqLibLogging();

    TestSuite suite = new TestSuite("Test for org.jaqlib");
    // $JUnit-BEGIN$
    suite.addTestSuite(ColumnDatabaseQBTest.class);
    suite.addTestSuite(BeanDatabaseQBTest.class);

    suite.addTest(JaqlibDbTests.suite());

    // $JUnit-END$
    return suite;
  }

}
