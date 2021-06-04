package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.JdkXPathEngine;
import org.junit.jupiter.api.BeforeEach;

public class XmlQBJdkTest extends XmlQBTest
{

  @BeforeEach
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new JdkXPathEngine());
  }

}
