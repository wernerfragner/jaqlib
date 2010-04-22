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

public class XmlSelectDataSource extends XmlDataSource implements
    SelectDataSource
{

  private XPathEngine xPathEngine = XmlDefaults.getXPathEngine();
  private String xPathExpression;
  private boolean useAttributes;
  private final XmlNamespaces namespaces = XmlDefaults.getNamespaces();


  public XmlSelectDataSource(Resource xmlPath)
  {
    this(xmlPath, true);
  }


  public XmlSelectDataSource(Resource xmlPath, boolean useAttributes)
  {
    super(xmlPath);
    this.useAttributes = useAttributes;
  }


  public void addAttributeNamespace(String prefix, String uri)
  {
    namespaces.add(prefix, uri);
  }


  public void setXPathEngine(XPathEngine xPathEngine)
  {
    this.xPathEngine = xPathEngine;
  }


  public void setXPathExpression(String xPathExpression)
  {
    this.xPathExpression = xPathExpression;
  }


  public void setUseAttributes(boolean useAttributes)
  {
    this.useAttributes = useAttributes;
  }


  public boolean isUseAttributes()
  {
    return useAttributes;
  }


  @Override
  public void closeAfterQuery()
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
  }


}
