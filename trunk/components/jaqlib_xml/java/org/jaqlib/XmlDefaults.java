package org.jaqlib;

import org.jaqlib.xml.xpath.JdkXPathEngine;
import org.jaqlib.xml.xpath.XPathEngine;

public class XmlDefaults
{

  private static XPathEngine xPathEngine = new JdkXPathEngine();


  public static XPathEngine getXPathEngine()
  {
    return xPathEngine;
  }

}
