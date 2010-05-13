package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.xml.xpath.SaxonXPathEngine;

public class XmlQBSaxonTest extends XmlQBTest
{

  @Override
  public void setUp()
  {
    super.setUp();
    XmlDefaults.INSTANCE.setXPathEngine(new SaxonXPathEngine());
  }

}
