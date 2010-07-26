package org.jaqlib.xml;

import java.util.logging.Logger;

import org.jaqlib.core.DsResultSet;
import org.jaqlib.core.bean.FieldMapping;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.util.LogUtil;
import org.jaqlib.xml.xpath.XmlNamespace;
import org.jaqlib.xml.xpath.XmlNamespaces;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents the 'raw' XML result of the query. This result set is used
 * internally to extract XML data and to map it to Java beans.
 * 
 * @author Werner Fragner
 */
public class XmlResultSet implements DsResultSet
{

  private final Logger log = LogUtil.getLogger(this);

  private final NodeList nodes;
  private final boolean useAttributes;
  private final XmlNamespaces namespaces;

  private int curNodeIndex = -1;


  /**
   * Default constructor.
   * 
   * @param nodes the 'raw' XML data. Can be null.
   * @param useAttributes if true, XML attributes are used to map data to Java
   *          bean fields.
   * @param namespaces the XML namespaces to use to extract the XML data.
   */
  public XmlResultSet(NodeList nodes, boolean useAttributes,
      XmlNamespaces namespaces)
  {
    if (nodes == null)
    {
      nodes = EmptyNodeList.INSTANCE;
    }
    this.nodes = nodes;

    this.useAttributes = useAttributes;

    if (namespaces == null)
    {
      namespaces = new XmlNamespaces();
    }
    this.namespaces = namespaces;

    log.fine("Created a new XMLResultSet with " + nodes.getLength()
        + " XML nodes. XML " + (this.useAttributes ? "attributes" : "elements")
        + " are used for field mapping.");
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject(FieldMapping<?> mapping)
  {
    String name = mapping.getSourceName();
    Node n = nodes.item(curNodeIndex);

    Node resultNode = null;
    if (useAttributes)
    {
      NamedNodeMap attributes = n.getAttributes();
      resultNode = getNamedAttribute(attributes, name);
    }
    else
    {
      resultNode = findNode(n, name);
      if (resultNode != null)
        resultNode = resultNode.getFirstChild();
    }

    if (resultNode == null)
    {
      String str = useAttributes ? "attribute" : "element";
      log.info("XML file does not contain an " + str + " named '" + name
          + "'; " + str + " is ignored.");
      return NO_RESULT;
    }

    return convert(mapping, resultNode.getNodeValue());
  }


  private Node getNamedAttribute(NamedNodeMap attributes, String name)
  {
    Node result = attributes.getNamedItem(name);
    if (result == null)
    {
      for (XmlNamespace ns : namespaces)
      {
        result = attributes.getNamedItemNS(ns.getUri(), name);
        if (result != null)
        {
          return result;
        }
      }
    }
    return result;
  }


  private Object convert(FieldMapping<?> mapping, String nodeValue)
  {
    if (nodeValue == null || nodeValue.trim().length() < 1)
    {
      return null;
    }

    JavaTypeHandler typeHandler = mapping.getTypeHandler();
    if (typeHandler != JavaTypeHandler.NULL)
    {
      return typeHandler.convert(nodeValue);
    }

    return defaultConvert(mapping, nodeValue);
  }


  private Object defaultConvert(FieldMapping<?> mapping, String nodeValue)
  {
    Class<?> fieldType = mapping.getFieldType();
    if (fieldType.equals(String.class))
    {
      return nodeValue;
    }
    else if (fieldType.equals(Integer.class))
    {
      return Integer.valueOf(nodeValue);
    }
    else if (fieldType.equals(Long.class))
    {
      return Long.valueOf(nodeValue);
    }
    else if (fieldType.equals(Double.class))
    {
      return Double.valueOf(nodeValue);
    }
    else if (fieldType.equals(Float.class))
    {
      return Float.valueOf(nodeValue);
    }
    else if (fieldType.equals(Byte.class))
    {
      return Byte.valueOf(nodeValue);
    }
    else if (fieldType.equals(char.class))
    {
      return new Character(nodeValue.charAt(0));
    }
    else if (fieldType.equals(Boolean.class))
    {
      return Boolean.valueOf(nodeValue);
    }

    throw new IllegalArgumentException("Unsupported field type '" + fieldType
        + "' for field '" + mapping.getTargetName() + "'.");
  }


  private Node findNode(Node n, String valueLabel)
  {
    NodeList childs = n.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++)
    {
      Node child = childs.item(i);
      if (child.getNodeName().equals(valueLabel))
      {
        return child;
      }
    }
    return null;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean next()
  {
    curNodeIndex++;
    return curNodeIndex < nodes.getLength();
  }

}
