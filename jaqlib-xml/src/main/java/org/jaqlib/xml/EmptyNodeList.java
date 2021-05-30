package org.jaqlib.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents an empty {@link NodeList}. This class can be used to avoid checks
 * for <tt>null</tt>.
 * 
 * @author Werner Fragner
 */
public class EmptyNodeList implements NodeList
{

  /**
   * Singleton instance of {@link EmptyNodeList}.
   */
  public static final EmptyNodeList INSTANCE = new EmptyNodeList();


  private EmptyNodeList()
  {
    throw new UnsupportedOperationException(
        "This class is not intended to be instantiated");
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getLength()
  {
    return 0;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Node item(int index)
  {
    return null;
  }

}
