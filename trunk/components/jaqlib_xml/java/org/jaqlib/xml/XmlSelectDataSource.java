package org.jaqlib.xml;

import org.jaqlib.XmlDefaults;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.core.SelectDataSource;
import org.jaqlib.util.FilePath;
import org.jaqlib.xml.xpath.XPathEngine;
import org.w3c.dom.NodeList;

public class XmlSelectDataSource extends XmlDataSource implements
    SelectDataSource
{

  private XPathEngine xPathEngine = XmlDefaults.getXPathEngine();
  private String xPathExpression;
  private final boolean useAttributes;


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


  public void setXPathExpression(String xPathExpression)
  {
    this.xPathExpression = xPathExpression;
  }


  @Override
  public void closeAfterQuery()
  {
    xPathEngine.close();
  }


  @Override
  public DsResultSet execute()
  {
    xPathEngine.open(getXmlPath());

    NodeList nodes = xPathEngine.getResults(xPathExpression);
    return new XmlResultSet(nodes, useAttributes);
  }


}
