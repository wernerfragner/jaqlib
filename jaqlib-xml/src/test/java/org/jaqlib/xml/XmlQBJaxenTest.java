package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.JaxenXPathEngine;
import org.junit.jupiter.api.BeforeEach;

public class XmlQBJaxenTest extends XmlQBTest
{

  @BeforeEach
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new JaxenXPathEngine());
  }

}
