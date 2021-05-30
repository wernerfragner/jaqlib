package org.jaqlib.xml.xpath;

import javax.xml.xpath.XPathConstants;

import net.sf.saxon.xpath.XPathFactoryImpl;

/**
 * Implementation of the {@link XPathEngine} interface for the <a
 * href="http://saxon.sourceforge.net/">Saxon</a> library. Note that this
 * implementation uses the JAXP API implementation of Saxon (see <a
 * href="http://www.saxonica.com/documentation/xpath-api/jaxp-xpath.html">Saxon
 * JAXP XPath API</a>). It does not use the standalone implementation (see <a
 * href="http://www.saxonica.com/documentation/xpath-api/standalone.html">
 * Standalone XPath API</a>).
 * 
 * @author Werner Fragner
 */
public class SaxonXPathEngine extends JaxpXPathEngine
{

  /**
   * Default constructor.
   */
  public SaxonXPathEngine()
  {
    setFactoryUri(XPathConstants.DOM_OBJECT_MODEL);
    setFactoryClassName(XPathFactoryImpl.class.getName());
  }

}
