package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;


public class JaqLibClassTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib");
    // $JUnit-BEGIN$
    suite.addTestSuite(IterableClassTest.class);
    suite.addTestSuite(CustomConditonClassTest.class);
    suite.addTestSuite(ReflectiveConditionClassTest.class);
    // $JUnit-END$
    return suite;
  }

}
