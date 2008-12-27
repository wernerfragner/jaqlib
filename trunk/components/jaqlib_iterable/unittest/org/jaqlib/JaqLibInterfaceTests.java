package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;


public class JaqLibInterfaceTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib");
    // $JUnit-BEGIN$
    suite.addTestSuite(SelectInterfaceTest.class);
    suite.addTestSuite(CustomConditonInterfaceTest.class);
    suite.addTestSuite(ReflectiveConditionInterfaceTest.class);
    // $JUnit-END$
    return suite;
  }

}
