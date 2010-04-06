package org.jaqlib.xml;

import org.jaqlib.util.FilePath;

public class XmlDataSource
{

  private final FilePath xmlPath;


  public XmlDataSource(FilePath xmlPath)
  {
    this.xmlPath = xmlPath;
  }


  public FilePath getXmlPath()
  {
    return xmlPath;
  }


}
