package org.jaqlib.xml.xpath;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jaqlib.util.ExceptionUtil;
import org.jaqlib.util.FilePath;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class JdkXPathEngine implements XPathEngine
{

  private FilePath xmlPath;
  private InputSource inputSource;
  private XPathFactory factory;


  public void open(FilePath xmlPath)
  {
    if (xmlPath.equals(this.xmlPath))
    {
      return;
    }
    this.xmlPath = xmlPath;

    if (inputSource != null)
    {
      close();
    }

    inputSource = new InputSource(xmlPath.getStream());
    factory = XPathFactory.newInstance();
  }


  public void close()
  {
    // try
    // {
    // if (inputSource != null)
    // {
    // inputSource.getByteStream().close();
    // }
    // }
    // catch (IOException ex)
    // {
    // throw ExceptionUtil.toRuntimeException(ex);
    // }
    // finally
    // {
    // inputSource = null;
    // }
  }


  public NodeList getResults(String expression)
  {
    try
    {
      XPath xpath = factory.newXPath();
      return (NodeList) xpath.evaluate(expression, inputSource,
          XPathConstants.NODESET);
    }
    catch (XPathExpressionException ex)
    {
      throw ExceptionUtil.toRuntimeException(ex);
    }
  }

}
