package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.JdkXPathEngine;

public class XmlQBJdkTest extends XmlQBTest
{

  @Override
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new JdkXPathEngine());
  }

}
