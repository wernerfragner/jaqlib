package org.jaqlib.xml.xpath;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.jaqlib.util.ExceptionUtil;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class XalanXPathEngine extends AbstractXPathEngine
{

  private Element root;


  @Override
  public void doOpen(DocumentBuilder builder)
  {
    addAttributeNamespaces(builder);
  }


  private void addAttributeNamespaces(DocumentBuilder builder)
  {
    if (isNamespaceAware())
    {
      DOMImplementation impl = builder.getDOMImplementation();
      Document namespaceHolder = impl.createDocument(null, null, null);
      root = namespaceHolder.getDocumentElement();

      for (AttributeNamespace namespace : getAttributeNamespaces())
      {
        root.setAttributeNS(namespace.getAttributeNsURI(), namespace
            .getAttributeNsPrefix(), namespace.getValue());
      }
    }
  }


  public NodeList getResults(String expression)
  {
    try
    {
      if (root != null)
      {
        return XPathAPI.selectNodeList(getDocument(), expression, root);
      }
      return XPathAPI.selectNodeList(getDocument(), expression);
    }
    catch (TransformerException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


}
