package org.jaqlib.xml.xpath;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.jaqlib.util.ExceptionUtil;
import org.w3c.dom.NodeList;

/**
 * Base class that uses the JAXP API and DOM trees (see <a
 * href="http://en.wikipedia.org/wiki/Java_API_for_XML_Processing" >JAXP on
 * Wikipedia</a>). This class can be configured with custom JAXP factory URIs
 * and factory classes. If nothing is configured then the default JDK XPath
 * engine is used.
 * 
 * @author Werner Fragner
 */
public class JaxpXPathEngine extends AbstractDomXPathEngine
{

  private XPathFactory factory;

  private String factoryUri = XPathFactory.DEFAULT_OBJECT_MODEL_URI;
  private String factoryClassName;
  private ClassLoader classLoader = Thread.currentThread()
      .getContextClassLoader();


  /**
   * Returns the JAXP factory URI. This URI is used to identify the factory. If
   * none is set then the default JDK JAXP factory URI is returned.
   * 
   * @return see description.
   */
  public String getFactoryUri()
  {
    return factoryUri;
  }


  /**
   * Sets the JAXP factory URI. This URI is used to identify the factory.
   * 
   * @param uri the JAXP factory URI.
   */
  public void setFactoryUri(String uri)
  {
    this.factoryUri = uri;
  }


  /**
   * Returns the fully qualified (i.e. with all packages) JAXP factory class
   * name.
   * 
   * @return see description.
   */
  public String getFactoryClassName()
  {
    return factoryClassName;
  }


  /**
   * Sets the fully qualified (i.e. with all packages) JAXP factory class name.
   * 
   * @param factoryClassName the JAXP factory class name.
   */
  public void setFactoryClassName(String factoryClassName)
  {
    this.factoryClassName = factoryClassName;
  }


  /**
   * Returns the class loader that is used to load the JAXP factory class (that
   * has been specified by {@link #setFactoryClassName(String)}).
   * 
   * @return see description.
   */
  public ClassLoader getClassLoader()
  {
    return classLoader;
  }


  /**
   * Sets the class loader that is used to load the JAXP factory class (that has
   * been specified by {@link #setFactoryClassName(String)}).
   * 
   * @param classLoader the class loader to use.
   */
  public void setClassLoader(ClassLoader classLoader)
  {
    this.classLoader = classLoader;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  protected void doOpen(DocumentBuilder builder)
  {
    try
    {
      factory = createFactory();
    }
    catch (XPathFactoryConfigurationException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  private XPathFactory createFactory()
      throws XPathFactoryConfigurationException
  {
    if (factoryUri == XPathFactory.DEFAULT_OBJECT_MODEL_URI)
    {
      return XPathFactory.newInstance();
    }
    else
    {
      return XPathFactory
          .newInstance(factoryUri, factoryClassName, classLoader);
    }
  }


  /**
   * {@inheritDoc}
   */
  public NodeList getResults(String expression)
  {
    try
    {
      XPath xpath = factory.newXPath();
      if (areNamespacesAvailable())
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
