package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jaqlib.xml.XmlQBJaxenTest;
import org.jaqlib.xml.XmlQBJaxpTest;
import org.jaqlib.xml.XmlQBJdkTest;
import org.jaqlib.xml.XmlQBSaxonTest;
import org.jaqlib.xml.XmlQBXalanTest;

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
