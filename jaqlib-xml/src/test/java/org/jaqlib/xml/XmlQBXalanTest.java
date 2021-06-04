package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.XalanXPathEngine;
import org.junit.jupiter.api.BeforeEach;

public class XmlQBXalanTest extends XmlQBTest
{

  @BeforeEach
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new XalanXPathEngine());
  }

}
