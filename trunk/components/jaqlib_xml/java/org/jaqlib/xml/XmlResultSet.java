package org.jaqlib.xml;

import org.jaqlib.core.DsResultSet;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlResultSet implements DsResultSet
{

  private final NodeList nodes;
  private final boolean useAttributes;

  private int curNodeIndex = 0;


  public XmlResultSet(NodeList nodes, boolean useAttributes)
  {
    if (nodes == null)
    {
      nodes = EmptyNodeList.INSTANCE;
    }
    this.nodes = nodes;
    this.useAttributes = useAttributes;
  }


  @Override
  public Object getObject(int valueDataType, int valueIndex)
  {
    Node n = nodes.item(curNodeIndex);

    Node result = null;
    if (useAttributes)
    {
      NamedNodeMap attributes = n.getAttributes();
      result = attributes.item(valueIndex);
    }
    else
    {
      result = findNode(n, valueIndex);
    }

    return result.getTextContent();
  }


  private Node findNode(Node n, int valueIndex)
  {
    return n.getChildNodes().item(valueIndex);
  }


  @Override
  public Object getObject(int valueDataType, String valueLabel)
  {
    Node n = nodes.item(curNodeIndex);

    Node result = null;
    if (useAttributes)
    {
      NamedNodeMap attributes = n.getAttributes();
      result = attributes.getNamedItem(valueLabel);
    }
    else
    {
      result = findNode(n, valueLabel);
    }

    return result.getNodeValue();
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


  @Override
  public boolean next()
  {
    curNodeIndex++;
    return curNodeIndex < nodes.getLength();
  }

}
