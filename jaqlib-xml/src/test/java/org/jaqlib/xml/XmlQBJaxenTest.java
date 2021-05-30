package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.JaxenXPathEngine;

public class XmlQBJaxenTest extends XmlQBTest
{

  @Override
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new JaxenXPathEngine());
  }

}
