package org.jaqlib.xml.xpath;

import javax.xml.XMLConstants;

/**
 * Holds all information about an XML namespace that can be applied to a XML
 * document.
 * 
 * @author Werner Fragner
 */
public class XmlNamespace
{

  public static final String XMLNS_ATTRIBUTE_NS_URI = XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
  public static final String XMLNS_ATTRIBUTE = XMLConstants.XMLNS_ATTRIBUTE;

  private String prefix;
  private String uri;


  public XmlNamespace(String prefix, String uri)
  {
    super();
    this.prefix = prefix;
    this.uri = uri;
  }


  public String getPrefix()
  {
    return prefix;
  }


  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }


  public String getUri()
  {
    return uri;
  }


  public void setUri(String uri)
  {
    this.uri = uri;
  }


  public String getDefaultUri()
  {
    return XMLNS_ATTRIBUTE_NS_URI;
  }


  public String getCompletePrefix()
  {
    return XMLNS_ATTRIBUTE + ":" + getPrefix();
  }


}
