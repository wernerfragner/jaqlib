package org.jaqlib.xml.xpath;

import javax.xml.XMLConstants;

/**
 * Holds all information about an XML attribute namespace that can be applied to
 * a XML document.
 * 
 * @author Werner Fragner
 */
public class AttributeNamespace
{

  private String prefix;
  private String value;


  public AttributeNamespace(String prefix, String value)
  {
    super();
    this.prefix = prefix;
    this.value = value;
  }


  public String getPrefix()
  {
    return prefix;
  }


  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }


  public String getValue()
  {
    return value;
  }


  public void setValue(String value)
  {
    this.value = value;
  }


  public String getAttributeNsURI()
  {
    return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
  }


  public String getAttributeNsPrefix()
  {
    return XMLConstants.XMLNS_ATTRIBUTE + ":" + getPrefix();
  }


}
