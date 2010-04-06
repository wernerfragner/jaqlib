package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.util.FilePath;
import org.jaqlib.xml.xpath.XPathEngine;

public class XmlSelectDataSource extends XmlDataSource
{

  private final boolean useAttributes;
  private XPathEngine xPathEngine = XmlDefaults.getXPathEngine();


  public XmlSelectDataSource(FilePath xmlPath)
  {
    this(xmlPath, true);
  }


  public XmlSelectDataSource(FilePath xmlPath, boolean useAttributes)
  {
    super(xmlPath);
    this.useAttributes = useAttributes;
  }


  public void setXPathEngine(XPathEngine xPathEngine)
  {
    this.xPathEngine = xPathEngine;
  }


  public XmlResultSet execute(String xPathExpression)
  {
    return new XmlResultSet(xPathEngine.getResults(xPathExpression));
  }

}
