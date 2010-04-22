package org.jaqlib;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jaqlib.db.JaqlibDbTests;

public class DatabaseQBTests
{

  public static Test suite()
  {
    // register unittest handler in order that log messages are built (-->
    // detect possible problems with building log messages)
    Logger.getLogger("org.jaqlib").addHandler(new UnittestHandler());
    Logger.getLogger("org.jaqlib").setLevel(Level.ALL);

    TestSuite suite = new TestSuite("Test for org.jaqlib");

    // $JUnit-BEGIN$
    suite.addTestSuite(ColumnDatabaseQBSelectTest.class);
    suite.addTestSuite(BeanDatabaseQBSelectTest.class);
    suite.addTestSuite(BeanDatabaseQBDmlTest.class);
    suite.addTestSuite(DbDefaultsTest.class);
    suite.addTest(JaqlibDbTests.suite());
    // $JUnit-END$
    return suite;
  }
}
