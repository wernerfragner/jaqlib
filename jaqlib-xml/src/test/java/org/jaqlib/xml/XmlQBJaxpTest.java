package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.JaxpXPathEngine;
import org.junit.jupiter.api.BeforeEach;

public class XmlQBJaxpTest extends XmlQBTest
{

  @BeforeEach
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new JaxpXPathEngine());
  }

}
