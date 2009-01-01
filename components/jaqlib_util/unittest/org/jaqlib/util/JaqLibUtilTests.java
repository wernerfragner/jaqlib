package org.jaqlib.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JaqLibUtilTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib.util");
    //$JUnit-BEGIN$
    suite.addTestSuite(ExceptionUtilTest.class);
    suite.addTestSuite(CompareUtilTest.class);
    suite.addTestSuite(CollectionUtilTest.class);
    suite.addTestSuite(ReflectionUtilTest.class);
    //$JUnit-END$
    return suite;
  }

}
