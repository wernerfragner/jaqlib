package org.jaqlib.xml.xpath;

import javax.xml.xpath.XPathFactory;


/**
 * Implementation of the {@link XPathEngine} interface for the JDK XPath engine.
 * 
 * @author Werner Fragner
 */
public class JdkXPathEngine extends JaxpXPathEngine
{

  /**
   * Default constructor.
   */
  public JdkXPathEngine()
  {
    setFactoryUri(XPathFactory.DEFAULT_OBJECT_MODEL_URI);
  }

}
