package org.jaqlib.query.syntaxtree;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public abstract class Connector<T> implements SyntaxTreeItem<T>
{

  private SyntaxTreeItem<T> left;
  private SyntaxTreeItem<T> right;


  void setLeft(SyntaxTreeItem<T> item)
  {
    this.left = item;
  }


  public void setRight(SyntaxTreeItem<T> item)
  {
    this.right = item;
  }


  public SyntaxTreeItem<T> getLeft()
  {
    return left;
  }


  public SyntaxTreeItem<T> getRight()
  {
    return right;
  }


}
