package org.jaqlib.xml;

import java.util.Collection;
import java.util.logging.Logger;

import org.jaqlib.XmlDefaults;
import org.jaqlib.core.DataSourceQueryException;
import org.jaqlib.core.Defaults;
import org.jaqlib.core.DsResultSet;
import org.jaqlib.core.QueryResultException;
import org.jaqlib.core.bean.BeanFieldMapping;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.CollectionFactory;
import org.jaqlib.core.bean.CollectionFieldMapping;
import org.jaqlib.core.bean.FieldMapping;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.util.LogUtil;
import org.jaqlib.util.lang.StringConversion;
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
   * @param nodes
   *          the 'raw' XML data. Can be null.
   * @param useAttributes
   *          if true, XML attributes are used to map data to Java bean fields.
   * @param namespaces
   *          the XML namespaces to use to extract the XML data.
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
  public Object getAnynomousObject(FieldMapping<?> mapping)
  {
    String original = mapping.getSourceName();
    mapping.setSourceName("#text"); // use #text node for anonymous value
    Object result = getObject(mapping);
    mapping.setSourceName(original);
    return result;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject(FieldMapping<?> mapping)
  {
    if (mapping instanceof CollectionFieldMapping)
    {
      return getCollectionObject((CollectionFieldMapping) mapping);
    }
    else if (mapping instanceof BeanFieldMapping)
    {
      return getBeanObject((BeanFieldMapping<?>) mapping);
    }
    else
    {
      return getPrimitiveObject(mapping);
    }
  }


  private Object getBeanObject(BeanFieldMapping<?> mapping)
  {
    Node current = getCurrentNode();
    Node match = getMatchingChildNode(mapping, current);

    NodeListImpl childNodes = new NodeListImpl();
    childNodes.add(match);

    XmlResultSet childResultSet = new XmlResultSet(childNodes, useAttributes,
        namespaces);

    if (childResultSet.next())
    {
      return mapping.getBeanMapping().getValue(childResultSet);
    }
    else
    {
      return handleNonExistingNode(current, mapping.getSourceName(), "element",
          true);
    }
  }


  private Node getMatchingChildNode(BeanFieldMapping<?> mapping, Node n)
  {
    String sourceName = mapping.getSourceName();

    Node result = null;

    NodeList childs = n.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++)
    {
      Node child = childs.item(i);
      if (child.getNodeName().equals(sourceName))
      {
        if (result == null)
        {
          result = child;
        }
        else
        {
          throw new QueryResultException(
              "Cannot perform mapping because multiple XML elements exist for '"
                  + mapping.getLogString()
                  + "'. Field should be a collection if multiple XML elements exist.");
        }
      }
    }
    return result;
  }


  private Object getCollectionObject(CollectionFieldMapping mapping)
  {
    Node current = getCurrentNode();
    NodeListImpl matches = getMatchingChildNodes(mapping, current);

    Collection result = createCollection(mapping);
    XmlResultSet childResultSet = new XmlResultSet(matches, useAttributes,
        namespaces);
    BeanMapping<?> elementMapping = mapping.getElementMapping();

    while (childResultSet.next())
    {
      Object value = elementMapping.getValue(childResultSet);
      result.add(value);
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
    // try to add all child nodes that matches the element source name

    String elementName = mapping.getElementSourceName();
    addMatchingChildNodes(elementName, n, matches);

    // try to add all child nodes that matches the collection source name

    String collectionName = mapping.getSourceName();
    NodeList children = n.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      if (child.getNodeName().equals(collectionName))
      {
        addMatchingChildNodes(mapping, child, matches);
      }
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


  private Object getPrimitiveObject(FieldMapping<?> mapping)
  {
    String sourceName = mapping.getSourceName();
    Node current = getCurrentNode();

    Node resultNode = null;
    if (useAttributes)
    {
      NamedNodeMap attributes = current.getAttributes();
      resultNode = getNamedAttribute(attributes, sourceName);

      if (resultNode == null)
      {
        handleNonExistingNode(current, sourceName, "attribute", false);
      }
    }

    if (resultNode == null)
    {
      resultNode = findNode(current, sourceName);
      if (resultNode != null && resultNode.getFirstChild() != null)
      {
        resultNode = resultNode.getFirstChild();
      }

      if (resultNode == null)
      {
        handleNonExistingNode(current, sourceName, "element", false);
      }
    }

    if (resultNode == null)
    {
      String elemAttr = useAttributes ? "attribute" : "element";
      return handleNonExistingNode(current, sourceName, elemAttr, true);
    }

    return convert(mapping, resultNode.getNodeValue());
  }


  private Node getNamedAttribute(NamedNodeMap attributes, String name)
  {
    if (attributes == null)
    {
      return null;
    }

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


  private Node findNode(Node n, String name)
  {
    NodeList childs = n.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++)
    {
      Node child = childs.item(i);
      if (child.getNodeName().equals(name))
      {
        return child;
      }
    }
    return null;
  }


  private Object handleNonExistingNode(Node current, String sourceName,
      String elemAttr, boolean shouldLog)
  {
    String msg = "XML file does not contain an XML " + elemAttr + " named '"
        + sourceName + "' (current XML node: " + getNodePath(current) + ")";

    if (XmlDefaults.INSTANCE.getStrictFieldCheck())
    {
      throw new DataSourceQueryException(msg);
    }

    if (shouldLog)
    {
      log.info(msg + "; " + elemAttr + " is ignored.");
    }
    return NO_RESULT;
  }


  private String getNodePath(Node current)
  {
    StringBuilder sb = new StringBuilder();
    appendNodePath(current, sb);
    return sb.toString();
  }


  private void appendNodePath(Node node, StringBuilder sb)
  {
    if (node != null)
    {
      appendNodePath(node.getParentNode(), sb);
      sb.append("/");
      sb.append(node.getNodeName());
    }
  }


  private Node getCurrentNode()
  {
    return nodes.item(curNodeIndex);
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
