package org.jaqlib.xml;

import javax.xml.XMLConstants;

/**
 * Holds all information about a XML namespace that can be applied to a XML
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


  /**
   * Gets the prefix of the namespace. E.g. jaqlib in the definition
   * 'xmlns:jaqlib=http://org.jaqlib/example'.
   * 
   * @return see description.
   */
  public String getPrefix()
  {
    return prefix;
  }


  /**
   * See param tag.
   * 
   * @param prefix the prefix of the namespace. E.g. jaqlib in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   */
  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }


  /**
   * Gets the URI of the namespace (can or cannot really exist). E.g.
   * 'http://org.jaqlib/example' in the definition
   * 'xmlns:jaqlib=http://org.jaqlib/example'.
   * 
   * @return see description.
   */
  public String getUri()
  {
    return uri;
  }


  /**
   * See param tag.
   * 
   * @param uri the URI of the namespace (can or cannot really exist). E.g.
   *          'http://org.jaqlib/example' in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   */
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
