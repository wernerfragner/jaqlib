package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.JaxpXPathEngine;

public class XmlQBJaxpTest extends XmlQBTest
{

  @Override
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new JaxpXPathEngine());
  }

}
