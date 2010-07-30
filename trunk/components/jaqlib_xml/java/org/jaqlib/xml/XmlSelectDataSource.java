package org.jaqlib.xml;

import java.io.IOException;

import org.jaqlib.XmlDefaults;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.core.SelectDataSource;
import org.jaqlib.util.ExceptionUtil;
import org.jaqlib.util.FileResource;
import org.jaqlib.util.Resource;
import org.jaqlib.util.StringUtil;
import org.jaqlib.xml.xpath.XPathEngine;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Datasource for selecting XML data. It holds all necessary information to
 * access a XML file and execute a query on it.
 * </p>
 * <p>
 * Note that the <tt>autoClose</tt> property can be used to improve performance
 * when querying the same XML file multiple times. By default the
 * <tt>autoClose</tt> property is set to <tt>true</tt>.
 * </p>
 * <p>
 * By default this class uses XML attributes to map XML data to Java bean
 * fields.
 * </p>
 * 
 * @author Werner Fragner
 */
public class XmlSelectDataSource extends XmlDataSource implements
    SelectDataSource
{

  private XPathEngine xPathEngine = XmlDefaults.INSTANCE.getXPathEngine();
  private String xPathExpression;
  private boolean useAttributes;


  /**
   * Constructs a new {@link XmlSelectDataSource} using the given XML file path.
   * XML attributes are used to map the XML data to Java bean fields.
   * 
   * @param xmlPath the path to the XML file.
   */
  public XmlSelectDataSource(String xmlPath)
  {
    this(new FileResource(xmlPath), true);
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
  public XmlSelectDataSource(String xmlPath, boolean useAttributes)
  {
    this(new FileResource(xmlPath), false);
  }


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
    if (isAutoClose())
    {
      close();
    }
  }


  /**
   * Closes this data source. This method releases used resources for reading
   * and parsing the XML file. This method only needs to be called by the Jaqlib
   * user when the property <tt>autoClose</tt> is set to false. Otherwise this
   * data source is automatically closed after executing the XML query.
   */
  public void close()
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
    if (StringUtil.isEmpty(this.xPathExpression))
    {
      throw new IllegalArgumentException(
          "No XPath expression was given. Use the 'where(String)' method to specify the XPath expression.");
    }

    try
    {
      xPathEngine.open(getXmlPath(), getNamespaces());

      NodeList nodes = xPathEngine.getResults(xPathExpression);
      return new XmlResultSet(nodes, useAttributes, getNamespaces());
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


  @Override
  public String toString()
  {
    return this.getXmlPath() + " [" + this.xPathExpression + "]";
  }

}
