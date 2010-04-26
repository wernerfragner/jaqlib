package org.jaqlib.xml.xpath;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jaqlib.util.ExceptionUtil;
import org.w3c.dom.NodeList;

/**
 * Implementation of the {@link XPathEngine} interface for the JDK XPath engine.
 * 
 * @author Werner Fragner
 */
public class JdkXPathEngine extends AbstractXPathEngine
{

  private XPathFactory factory;


  /**
   * {@inheritDoc}
   */
  @Override
  protected void doOpen(DocumentBuilder builder)
  {
    factory = XPathFactory.newInstance();
  }


  /**
   * {@inheritDoc}
   */
  public NodeList getResults(String expression)
  {
    try
    {
      XPath xpath = factory.newXPath();
      if (isNamespaceAware())
      {
        xpath.setNamespaceContext(getNamespaceContext());
      }

      return (NodeList) xpath.evaluate(expression, getDocument(),
          XPathConstants.NODESET);
    }
    catch (XPathExpressionException ex)
    {
      throw ExceptionUtil.toRuntimeException(ex);
    }
  }


  private NamespaceContext getNamespaceContext()
  {
    return new NamespaceContext()
    {

      /**
       * {@inheritDoc}
       */
      @Override
      @SuppressWarnings("unchecked")
      public Iterator getPrefixes(String namespace)
      {
        return getXmlNamespaces().findPrefixes(namespace).iterator();
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public String getPrefix(String namespace)
      {
        return getXmlNamespaces().findPrefix(namespace);
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public String getNamespaceURI(String prefix)
      {
        return getXmlNamespaces().findNamespace(prefix);
      }
    };
  }


}
