package org.jaqlib.xml.xpath;

import org.jaqlib.util.Resource;
import org.jaqlib.xml.XmlNamespaces;
import org.w3c.dom.NodeList;

import java.io.IOException;

/**
 * Abstraction for arbitrary XPath engines. Examples are Saxon, Xalan or the
 * built-in XPath engine in the JDK.
 *
 * @author Werner Fragner
 */
public interface XPathEngine
{

  /**
   * Opens the given XML file using the given namespaces.
   * 
   * @param xmlPath a not null resource pointing to an XML file.
   * @param namespaces a not null object containing all namespaces that should
   *          be applied.
   * @throws IOException in case a problem accessing or parsing the XML file
   *           occurs.
   */
  void open(Resource xmlPath, XmlNamespaces namespaces) throws IOException;


  /**
   * Closes the engine. That means that used resources are cleaned up.
   * 
   * @throws IOException in case a problem closing resources occurs.
   */
  void close() throws IOException;


  /**
   * Gets all DOM nodes that match the given expression.
   * 
   * @param xpathExpression an XPath expression.
   * @return all nodes that match the given expression.
   * @throws IOException in case a problem accessing or parsing the XML file
   *           occurs.
   */
  NodeList getResults(String xpathExpression) throws IOException;

}