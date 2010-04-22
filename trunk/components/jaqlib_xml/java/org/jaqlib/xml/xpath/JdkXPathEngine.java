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

public class JdkXPathEngine extends AbstractXPathEngine
{

  private XPathFactory factory;


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

      @Override
      @SuppressWarnings("unchecked")
      public Iterator getPrefixes(String namespace)
      {
        return getXmlNamespaces().findPrefixes(namespace).iterator();
      }


      @Override
      public String getPrefix(String namespace)
      {
        return getXmlNamespaces().findPrefix(namespace);
      }


      @Override
      public String getNamespaceURI(String prefix)
      {
        return getXmlNamespaces().findNamespace(prefix);
      }
    };
  }


  @Override
  protected void doOpen(DocumentBuilder builder)
  {
    factory = XPathFactory.newInstance();
  }

}
