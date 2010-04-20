package org.jaqlib.xml;

import org.jaqlib.util.Resource;

public class XmlDataSource
{

  private final Resource xmlPath;


  public XmlDataSource(Resource xmlPath)
  {
    this.xmlPath = xmlPath;
  }


  public Resource getXmlPath()
  {
    return xmlPath;
  }


}
