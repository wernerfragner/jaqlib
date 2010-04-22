package org.jaqlib;

import org.jaqlib.core.DefaultsDelegate;
import org.jaqlib.util.Assert;
import org.jaqlib.xml.xpath.JdkXPathEngine;
import org.jaqlib.xml.xpath.XPathEngine;
import org.jaqlib.xml.xpath.XmlNamespace;
import org.jaqlib.xml.xpath.XmlNamespaces;

/**
 * Static helper class that holds default infrastructure component instances and
 * global properties.<br>
 * <b>NOTE: Changes to these components/properties have an effect on the entire
 * application. Use with care!</b>
 * 
 * @author Werner Fragner
 */
public class XmlDefaults extends DefaultsDelegate
{

  /**
   * This class must only be instantiated as singleton.
   */
  private XmlDefaults()
  {
  }

  /**
   * Singleton instance of this class.
   */
  public static final XmlDefaults INSTANCE = new XmlDefaults();

  private XPathEngine xPathEngine;
  private XmlNamespaces namespaces;


  static
  {
    INSTANCE.reset();
  }


  public void reset()
  {
    super.reset();
    namespaces = new XmlNamespaces();
    xPathEngine = new JdkXPathEngine();
  }


  public XPathEngine getXPathEngine()
  {
    return xPathEngine;
  }


  public void setXPathEngine(XPathEngine xPathEngine)
  {
    this.xPathEngine = Assert.notNull(xPathEngine);
  }


  public XmlNamespaces getNamespaces()
  {
    return namespaces;
  }


  public void addNamespace(String prefix, String uri)
  {
    namespaces.add(new XmlNamespace(prefix, uri));
  }


  public void clearNamespaces()
  {
    namespaces.clear();
  }

}
