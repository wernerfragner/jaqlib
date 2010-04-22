package org.jaqlib.core.syntaxtree;

import org.jaqlib.util.ReflectionUtil;

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


  public void appendLogString(StringBuilder sb)
  {
    if (!(getLeft() instanceof NullSyntraxTreeNode<?>))
    {
      getLeft().appendLogString(sb);
      sb.append(" ");
      sb.append(ReflectionUtil.getPlainClassName(this).toUpperCase());
    }
    sb.append(" ");
    getRight().appendLogString(sb);
  }

}
