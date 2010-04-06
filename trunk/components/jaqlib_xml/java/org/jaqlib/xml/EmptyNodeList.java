package org.jaqlib.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EmptyNodeList implements NodeList
{

  public static final EmptyNodeList INSTANCE = new EmptyNodeList();


  @Override
  public int getLength()
  {
    return 0;
  }


  @Override
  public Node item(int index)
  {
    return null;
  }

}
