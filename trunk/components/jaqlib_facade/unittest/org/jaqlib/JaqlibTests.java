package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JaqlibTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib");
    // $JUnit-BEGIN$

    suite.addTest(IterableQBClassTests.suite());
    suite.addTest(IterableQBInterfaceTests.suite());
    suite.addTest(DatabaseQBTests.suite());
    suite.addTest(XmlQBTests.suite());

    // $JUnit-END$
    return suite;
  }

}
