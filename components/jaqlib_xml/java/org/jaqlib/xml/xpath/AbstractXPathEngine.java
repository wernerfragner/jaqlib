package org.jaqlib.xml.xpath;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jaqlib.util.ExceptionUtil;
import org.jaqlib.util.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public abstract class AbstractXPathEngine implements XPathEngine
{

  private Resource xmlPath;
  private InputSource inputSource;

  private Document doc;
  private final AttributeNamespaces attributeNamespaces = new AttributeNamespaces();


  public void addAttributeNamespace(AttributeNamespace namespace)
  {
    attributeNamespaces.add(namespace);
  }


  protected AttributeNamespaces getAttributeNamespaces()
  {
    return attributeNamespaces;
  }


  public final void open(Resource xmlPath)
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

    inputSource = new InputSource(getInputStream());
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(isNamespaceAware());
      DocumentBuilder builder = factory.newDocumentBuilder();

      doc = builder.parse(inputSource);

      doOpen(builder);
    }
    catch (Exception e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  protected Document getDocument()
  {
    return doc;
  }


  public boolean isNamespaceAware()
  {
    return !attributeNamespaces.isEmpty();
  }


  protected abstract void doOpen(DocumentBuilder builder);


  public void close()
  {
    // try
    // {
    // if (inputSource != null)
    // {
    // inputSource.getByteStream().close();
    // }
    // }
    // catch (IOException ex)
    // {
    // throw ExceptionUtil.toRuntimeException(ex);
    // }
    // finally
    // {
    // inputSource = null;
    // }
  }


  protected InputSource getInputSource()
  {
    return inputSource;
  }


  private InputStream getInputStream()
  {
    try
    {
      return xmlPath.getInputStream();
    }
    catch (IOException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }

}
