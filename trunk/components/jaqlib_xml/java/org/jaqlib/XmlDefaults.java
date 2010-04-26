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

  /**
   * Resets all defaults to their initial values.
   */
  static
  {
    INSTANCE.reset();
  }


  /**
   * Resets all defaults to their initial values.
   */
  @Override
  public void reset()
  {
    super.reset();
    namespaces = new XmlNamespaces();
    xPathEngine = new JdkXPathEngine();
  }


  /**
   * Gets the current default {@link XPathEngine}.
   * 
   * @return see description.
   */
  public XPathEngine getXPathEngine()
  {
    return xPathEngine;
  }


  /**
   * Sets the new application-wide {@link XPathEngine}.
   * 
   * @param xPathEngine the new {@link XPathEngine}.
   */
  public void setXPathEngine(XPathEngine xPathEngine)
  {
    this.xPathEngine = Assert.notNull(xPathEngine);
  }


  /**
   * Gets the current default XML namespaces.
   * 
   * @return see description.
   */
  public XmlNamespaces getNamespaces()
  {
    return namespaces;
  }


  /**
   * Adds the given namespace to the application-wide defaults.
   * 
   * @param prefix the prefix of the namespace (e.g., 'ns').
   * @param uri the URI for the namespace (e.g., 'http://jaqlib.org/myns').
   */
  public void addNamespace(String prefix, String uri)
  {
    namespaces.add(new XmlNamespace(prefix, uri));
  }


  /**
   * Removes all previously added namespaces.
   */
  public void clearNamespaces()
  {
    namespaces.clear();
  }

}
