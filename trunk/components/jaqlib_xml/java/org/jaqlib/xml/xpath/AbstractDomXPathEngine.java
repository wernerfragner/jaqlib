package org.jaqlib.xml.xpath;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaqlib.util.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Abstract base class for {@link XPathEngine}s. It provides basic XML XPath
 * functionality for providing an XML DOM tree.
 * 
 * @author Werner Fragner
 */
public abstract class AbstractDomXPathEngine implements XPathEngine
{

  private Resource xmlPath;
  private InputSource inputSource;
  private Document doc;
  private XmlNamespaces namespaces;


  /**
   * {@inheritDoc}
   */
  public final void open(Resource xmlPath, XmlNamespaces namespaces)
      throws IOException
  {
    if (xmlPath.equals(this.xmlPath))
    {
      return;
    }
    this.xmlPath = xmlPath;

    if (inputSource != null)
    {
      close();
    }

    this.namespaces = namespaces;

    inputSource = new InputSource(getInputStream());
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(areNamespacesAvailable());

    try
    {
      DocumentBuilder builder = factory.newDocumentBuilder();
      doc = builder.parse(inputSource);
      doOpen(builder);
    }
    catch (SAXException x)
    {
      throw toIOException(x);
    }
    catch (ParserConfigurationException x)
    {
      throw toIOException(x);
    }
  }


  /**
   * Gets the W3C DOM XML document.
   * 
   * @return see description.
   */
  protected Document getDocument()
  {
    return doc;
  }


  /**
   * Returns the {@link InputSource} representing the XML file.
   * 
   * @return see description.
   */
  protected InputSource getInputSource()
  {
    return inputSource;
  }


  /**
   * Gets all XML namespaces that have been registered by the Jaqlib user.
   * 
   * @return see description.
   */
  protected XmlNamespaces getXmlNamespaces()
  {
    return namespaces;
  }


  /**
   * Returns <tt>true</tt> if user-defined XML namespaces are available.
   * 
   * @return see description.
   */
  protected boolean areNamespacesAvailable()
  {
    return !namespaces.isEmpty();
  }


  /**
   * Extending classes must override this method in order to perform tasks for
   * opening the XML file.
   * 
   * @param builder the builder that has been used to build the W3C DOM tree.
   */
  protected abstract void doOpen(DocumentBuilder builder);


  /**
   * {@inheritDoc}
   */
  public void close()
  {
  }


  private InputStream getInputStream() throws IOException
  {
    if (!xmlPath.exists())
    {
      throw new IOException("Resource '" + xmlPath + "' not found.");
    }
    return xmlPath.getInputStream();
  }


  private static IOException toIOException(Throwable t)
  {
    IOException ioe = new IOException(t);
    ioe.setStackTrace(t.getStackTrace());
    return ioe;
  }

}
