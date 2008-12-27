package org.jaqlib.query.syntaxtree;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public abstract class Connector<T> implements SyntaxTreeNode<T>
{

  private SyntaxTreeNode<T> left;
  private SyntaxTreeNode<T> right;


  void setLeft(SyntaxTreeNode<T> node)
  {
    this.left = node;
  }


  public void setRight(SyntaxTreeNode<T> node)
  {
    this.right = node;
  }


  public SyntaxTreeNode<T> getLeft()
  {
    return left;
  }


  public SyntaxTreeNode<T> getRight()
  {
    return right;
  }


}
