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


  /**
   * Constructs a new XML namespace object. This namespace is used to lookup XML
   * attribute or element values.
   * 
   * @param prefix the prefix of the namespace. E.g. jaqlib in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   * @param uri the URI of the namespace (can or cannot really exist). E.g.
   *          'http://org.jaqlib/example' in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   */
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


  /**
   * Gets the URI for the default XML namespace. Normally this is
   * "http://www.w3.org/2000/xmlns/".
   * 
   * @return see description.
   */
  public String getDefaultUri()
  {
    return XMLNS_ATTRIBUTE_NS_URI;
  }


  /**
   * Gets the complete XML namespace prefix (including {@link #XMLNS_ATTRIBUTE}.
   * For example, 'xmlns:jaqlib'.
   * 
   * @return see description.
   */
  public String getCompletePrefix()
  {
    return XMLNS_ATTRIBUTE + ":" + getPrefix();
  }


}
