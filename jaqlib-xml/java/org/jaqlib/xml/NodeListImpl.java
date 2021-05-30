package org.jaqlib.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Simple implementation of the {@link NodeList} interface. It provides methods
 * for adding and getting {@link Node} instances.
 * 
 * @author Werner Fragner
 */
public class NodeListImpl implements NodeList
{

  private final List<Node> nodes = new ArrayList<Node>();


  /**
   * Adds the given node to this list.
   * 
   * @param node the node to add; if null is given then no action is performed.
   */
  public void add(Node node)
  {
    if (node != null)
    {
      nodes.add(node);
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Node item(int index)
  {
    return nodes.get(index);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getLength()
  {
    return nodes.size();
  }

}
