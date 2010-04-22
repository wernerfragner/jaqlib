package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.XalanXPathEngine;

public class XmlQBXalanTest extends XmlQBTest
{

  @Override
  public void setUp()
  {
    super.setUp();
    XmlDefaults.setXPathEngine(new XalanXPathEngine());
  }

}
