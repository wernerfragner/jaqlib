package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.SaxonXPathEngine;
import org.junit.jupiter.api.BeforeEach;

public class XmlQBSaxonTest extends XmlQBTest
{

  @BeforeEach
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new SaxonXPathEngine());
  }

}
