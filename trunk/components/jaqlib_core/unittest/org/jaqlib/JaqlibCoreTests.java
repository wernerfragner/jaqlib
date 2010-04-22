package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JaqlibCoreTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib");
    // $JUnit-BEGIN$
    suite.addTestSuite(DefaultsTest.class);
    suite.addTestSuite(BeanConventionMappingStrategyTest.class);
    // $JUnit-END$
    return suite;
  }

}
