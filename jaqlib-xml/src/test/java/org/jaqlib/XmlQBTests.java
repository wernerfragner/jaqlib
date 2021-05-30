package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jaqlib.xml.*;

public class XmlQBTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib.xml");
    // $JUnit-BEGIN$
    suite.addTestSuite(XmlQBJaxpTest.class);
    suite.addTestSuite(XmlQBXalanTest.class);
    suite.addTestSuite(XmlQBJdkTest.class);
    suite.addTestSuite(XmlQBJaxenTest.class);
    suite.addTestSuite(XmlQBSaxonTest.class);
    // $JUnit-END$
    return suite;
  }

}
