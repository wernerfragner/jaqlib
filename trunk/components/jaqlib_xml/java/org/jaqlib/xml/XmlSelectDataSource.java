package org.jaqlib.xml;

import java.io.IOException;

import org.jaqlib.XmlDefaults;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.core.SelectDataSource;
import org.jaqlib.util.ExceptionUtil;
import org.jaqlib.util.Resource;
import org.jaqlib.xml.xpath.XPathEngine;
import org.jaqlib.xml.xpath.XmlNamespaces;
import org.w3c.dom.NodeList;

/**
 * Datasource for selecting XML data. It holds all necessary information to
 * access a XML file and execute a query on it.
 * 
 * @author Werner Fragner
 */
public class XmlSelectDataSource extends XmlDataSource implements
    SelectDataSource
{

  private XPathEngine xPathEngine = XmlDefaults.INSTANCE.getXPathEngine();
  private String xPathExpression;
  private boolean useAttributes;
  private final XmlNamespaces namespaces = XmlDefaults.INSTANCE.getNamespaces();


  /**
   * Constructs a new {@link XmlSelectDataSource} using the given XML file path.
   * XML attributes are used to map the XML data to Java bean fields.
   * 
   * @param xmlPath the path to the XML file.
   */
  public XmlSelectDataSource(Resource xmlPath)
  {
    this(xmlPath, true);
  }


  /**
   * Constructs a new {@link XmlSelectDataSource} using the given XML file path.
   * If <tt>useAttributes</tt> is <tt>true</tt> then XML attributes are used to
   * map the XML data to Java bean fields. If <tt>false</tt> XML elements are
   * used for mapping.
   * 
   * @param xmlPath the path to the XML file.
   * @param useAttributes if <tt>true</tt> then XML attributes are used to map
   *          the XML data to Java bean fields. If <tt>false</tt> XML elements
   *          are used for mapping.
   */
  public XmlSelectDataSource(Resource xmlPath, boolean useAttributes)
  {
    super(xmlPath);
    this.useAttributes = useAttributes;
  }


  /**
   * Adds a new XML namespace to this data source. This namespace is used to
   * lookup XML attribute or element values.
   * 
   * @param prefix the prefix of the namespace. E.g. jaqlib in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   * @param uri the URI of the namespace (can or cannot really exist). E.g.
   *          'http://org.jaqlib/example' in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   */
  public void addAttributeNamespace(String prefix, String uri)
  {
    namespaces.add(prefix, uri);
  }


  /**
   * Sets a custom XPath engine. If none is set then the default JDK XPath
   * engine is used.
   * 
   * @see XmlDefaults#setXPathEngine(XPathEngine)
   * @param xPathEngine the custom XPath engine.
   */
  public void setXPathEngine(XPathEngine xPathEngine)
  {
    this.xPathEngine = xPathEngine;
  }


  /**
   * Sets the XPath expression that should be used for querying the XML file.
   * 
   * @param xPathExpression a valid XPath expression (see <a href="http
   *          ://www.w3.org/TR/xpath/">W3C XPath Language</a>).
   */
  public void setXPathExpression(String xPathExpression)
  {
    this.xPathExpression = xPathExpression;
  }


  /**
   * See param tag.
   * 
   * @param useAttributes if <tt>true</tt> then XML attributes are used to map
   *          the XML data to Java bean fields. If <tt>false</tt> XML elements
   *          are used for mapping.
   */
  public void setUseAttributes(boolean useAttributes)
  {
    this.useAttributes = useAttributes;
  }


  /**
   * See return tag.
   * 
   * @return <tt>true</tt> then XML attributes are used to map the XML data to
   *         Java bean fields. If <tt>false</tt> XML elements are used for
   *         mapping.
   */
  public boolean isUseAttributes()
  {
    return useAttributes;
  }


  private void closeAfterQuery()
  {
    try
    {
      xPathEngine.close();
    }
    catch (IOException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public DsResultSet execute()
  {
    try
    {
      xPathEngine.open(getXmlPath(), namespaces);

      NodeList nodes = xPathEngine.getResults(xPathExpression);
      return new XmlResultSet(nodes, useAttributes, namespaces);
    }
    catch (IOException e)
    {
      throw ExceptionUtil.toRuntimeException(e);
    }
    finally
    {
      closeAfterQuery();
    }
  }

}
