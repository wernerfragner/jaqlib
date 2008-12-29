package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;

public class DatabaseQBTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib");
    // $JUnit-BEGIN$
    suite.addTestSuite(PrimitiveDatabaseQBTest.class);
    suite.addTestSuite(BeanDatabaseQBTest.class);
    // $JUnit-END$
    return suite;
  }

}
