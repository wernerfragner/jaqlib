package org.jaqlib.xml;

import java.util.Collection;
import java.util.logging.Logger;

import org.jaqlib.core.Defaults;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.CollectionFactory;
import org.jaqlib.core.bean.CollectionFieldMapping;
import org.jaqlib.core.bean.FieldMapping;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.util.LogUtil;
import org.jaqlib.util.lang.StringConversion;
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
    Node node = nodes.item(curNodeIndex);

    if (mapping instanceof CollectionFieldMapping)
    {
      return getCollectionObject((CollectionFieldMapping) mapping, node);
    }
    else
    {
      return getPrimitiveObject(mapping, node);
    }
  }


  private Object getCollectionObject(CollectionFieldMapping mapping, Node n)
  {
    NodeListImpl matches = getMatchingChildNodes(mapping, n);

    Collection result = createCollection(mapping);
    XmlResultSet childResultSet = new XmlResultSet(matches, useAttributes,
        namespaces);
    BeanMapping<?> elementMapping = mapping.getElementMapping();

    while (childResultSet.next())
    {
      Object value = elementMapping.getValue(childResultSet);
      result.add(elementMapping.getValue(childResultSet));
    }

    return result;
  }


  private NodeListImpl getMatchingChildNodes(CollectionFieldMapping mapping,
      Node n)
  {
    NodeListImpl matches = new NodeListImpl();
    addMatchingChildNodes(mapping, n, matches);
    return matches;
  }


  private void addMatchingChildNodes(CollectionFieldMapping mapping, Node n,
      NodeListImpl matches)
  {
    String name = mapping.getSourceName();

    if (mapping.hasPluralName())
    {
      NodeList children = n.getChildNodes();

      // try to add all child nodes that matches the singular

      String singular = mapping.getSingularName();
      addMatchingChildNodes(singular, n, matches);

      // try to add all child nodes that matches the plural

      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        if (child.getNodeName().equals(name))
        {
          addMatchingChildNodes(mapping, child, matches);
        }
      }
    }
    else
    {
      // try to add all child nodes that matches the singular

      addMatchingChildNodes(name, n, matches);
    }
  }


  private void addMatchingChildNodes(String name, Node n, NodeListImpl matches)
  {
    NodeList children = n.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      if (child.getNodeName().equals(name))
      {
        matches.add(child);
      }
    }
  }


  private Collection<?> createCollection(CollectionFieldMapping mapping)
  {
    Class<? extends Collection<?>> fieldType = mapping.getFieldType();
    return getCollectionFactory().newInstance(fieldType);
  }


  private CollectionFactory getCollectionFactory()
  {
    return Defaults.getCollectionFactory();
  }


  private Object getPrimitiveObject(FieldMapping<?> mapping, Node n)
  {
    String name = mapping.getSourceName();

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
    return StringConversion.fromString(fieldType, nodeValue);
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
