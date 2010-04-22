package org.jaqlib;

import org.jaqlib.util.Assert;
import org.jaqlib.xml.xpath.XmlNamespace;
import org.jaqlib.xml.xpath.XmlNamespaces;
import org.jaqlib.xml.xpath.JdkXPathEngine;
import org.jaqlib.xml.xpath.XPathEngine;

public class XmlDefaults
{

  private static XPathEngine xPathEngine;
  private static XmlNamespaces namespaces;


  static
  {
    reset();
  }


  public static void reset()
  {
    namespaces = new XmlNamespaces();
    xPathEngine = new JdkXPathEngine();
  }


  public static XPathEngine getXPathEngine()
  {
    return xPathEngine;
  }


  public static void setXPathEngine(XPathEngine xPathEngine)
  {
    XmlDefaults.xPathEngine = Assert.notNull(xPathEngine);
  }


  public static XmlNamespaces getNamespaces()
  {
    return namespaces;
  }


  public static void addNamespace(String prefix, String uri)
  {
    namespaces.add(new XmlNamespace(prefix, uri));
  }


  public static void clearNamespaces()
  {
    namespaces.clear();
  }


}
