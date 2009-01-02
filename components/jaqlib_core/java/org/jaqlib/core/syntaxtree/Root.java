package org.jaqlib.core.syntaxtree;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class Root<T> extends And<T>
{

  public Root()
  {
    setLeft(new NullSyntraxTreeNode<T>());
    setRight(new NullSyntraxTreeNode<T>());
  }

}
