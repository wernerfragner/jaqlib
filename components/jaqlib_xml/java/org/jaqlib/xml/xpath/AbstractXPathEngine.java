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

public abstract class AbstractXPathEngine implements XPathEngine
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
    factory.setNamespaceAware(isNamespaceAware());

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


  protected Document getDocument()
  {
    return doc;
  }


  protected XmlNamespaces getXmlNamespaces()
  {
    return namespaces;
  }


  protected boolean isNamespaceAware()
  {
    return !namespaces.isEmpty();
  }


  protected abstract void doOpen(DocumentBuilder builder);


  /**
   * {@inheritDoc}
   */
  public void close()
  {
  }


  protected InputSource getInputSource()
  {
    return inputSource;
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
