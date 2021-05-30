package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.util.Resource;

/**
 * Abstract base class for XML data sources.
 * 
 * @see XmlSelectDataSource
 * @author Werner Fragner
 */
public abstract class XmlDataSource
{

  private final Resource xmlPath;
  private final XmlNamespaces namespaces = XmlDefaults.INSTANCE.getNamespaces();
  private boolean autoClose = true;


  /**
   * Default constructor.
   * 
   * @param xmlPath a resource pointing to a XML file.
   */
  public XmlDataSource(Resource xmlPath)
  {
    this.xmlPath = xmlPath;
  }


  /**
   * Returns the resource pointing to a XML file.
   * 
   * @return see description.
   */
  public Resource getXmlPath()
  {
    return xmlPath;
  }


  /**
   * <p>
   * Returns true if this data source closes itself after executing the XML
   * query.
   * </p>
   * By default auto close is set to true.
   * 
   * @return see description.
   */
  public boolean isAutoClose()
  {
    return autoClose;
  }


  /**
   * <p>
   * If set to true this data source closes itself after executing the XML
   * query. If set to false the Jaqlib user must close this data source by
   * calling {@link #close()}. Otherwise used resources for reading and parsing
   * the XML file are <b>NOT</b> freed!
   * </p>
   * By default auto close is set to true.
   * 
   * @param autoClose see description
   */
  public void setAutoClose(boolean autoClose)
  {
    this.autoClose = autoClose;
  }


  /**
   * Closes this data source. This method releases used resources for reading
   * and parsing the XML file. This method only needs to be called by the Jaqlib
   * user when the property <tt>autoClose</tt> is set to false. Otherwise this
   * data source is automatically closed after executing the XML query.
   */
  public abstract void close();


  /**
   * Adds a new XML namespace to this data source. This namespace is used to
   * lookup XML attribute or element values.
   * 
   * @see XmlDefaults#addNamespace(String, String)
   * @param prefix the prefix of the namespace. E.g. jaqlib in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   * @param uri the URI of the namespace (can or cannot really exist). E.g.
   *          'http://org.jaqlib/example' in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   */
  public void addNamespace(String prefix, String uri)
  {
    namespaces.add(prefix, uri);
  }


  protected XmlNamespaces getNamespaces()
  {
    return namespaces;
  }

}
