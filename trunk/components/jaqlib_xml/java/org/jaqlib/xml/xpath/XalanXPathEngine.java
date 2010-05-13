package org.jaqlib.xml.xpath;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.jaqlib.util.ExceptionUtil;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Implementation of the {@link XPathEngine} interface for the <a
 * href="http://xalan.apache.org/">Xalan</a> library.
 * 
 * @author Werner Fragner
 */
public class XalanXPathEngine extends AbstractDomXPathEngine
{

  private Element root;


  /**
   * {@inheritDoc}
   */
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
      Document namespaceHolder = impl.createDocument(
          XmlNamespace.XMLNS_ATTRIBUTE_NS_URI, XmlNamespace.XMLNS_ATTRIBUTE,
          null);
      root = namespaceHolder.getDocumentElement();

      for (XmlNamespace namespace : getXmlNamespaces())
      {
        root.setAttributeNS(namespace.getDefaultUri(), namespace
            .getCompletePrefix(), namespace.getUri());
      }
    }
  }


  /**
   * {@inheritDoc}
   */
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
