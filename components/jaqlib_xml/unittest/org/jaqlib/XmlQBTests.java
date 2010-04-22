package org.jaqlib;

import org.jaqlib.xml.XmlQBJdkTest;
import org.jaqlib.xml.XmlQBXalanTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class XmlQBTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib.xml");
    //$JUnit-BEGIN$
    suite.addTestSuite(XmlQBXalanTest.class);
    suite.addTestSuite(XmlQBJdkTest.class);
    //$JUnit-END$
    return suite;
  }

}
