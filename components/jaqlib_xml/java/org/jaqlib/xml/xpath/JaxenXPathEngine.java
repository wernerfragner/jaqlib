package org.jaqlib.xml.xpath;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.jaqlib.util.ExceptionUtil;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Implementation of the {@link XPathEngine} interface for the <a
 * href="http://jaxen.codehaus.org/">Jaxen</a> library.
 * 
 * @author Werner Fragner
 */
public class JaxenXPathEngine extends AbstractDomXPathEngine
{

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doOpen(DocumentBuilder builder)
  {
    // nothing to open
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public NodeList getResults(String xpathExpression)
  {
    try
    {
      return getNodeList(xpathExpression);
    }
    catch (JaxenException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  private NodeList getNodeList(String xpathExpression) throws JaxenException
  {
    XPath expression = new org.jaxen.dom.DOMXPath(xpathExpression);
    addNamespaces(expression);
    List<?> results = expression.selectNodes(getDocument());
    return toNodeList(results);
  }


  private NodeList toNodeList(final List<?> results)
  {
    return new NodeList()
    {

      @Override
      public int getLength()
      {
        return results.size();
      }


      @Override
      public Node item(int index)
      {
        return (Node) results.get(index);
      }
    };
  }


  private void addNamespaces(XPath xpath) throws JaxenException
  {
    for (XmlNamespace namespace : getXmlNamespaces())
    {
      xpath.addNamespace(namespace.getPrefix(), namespace.getUri());
    }
  }

}
