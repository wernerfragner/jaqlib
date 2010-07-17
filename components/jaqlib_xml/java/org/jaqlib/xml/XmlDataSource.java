package org.jaqlib.xml;

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

}
