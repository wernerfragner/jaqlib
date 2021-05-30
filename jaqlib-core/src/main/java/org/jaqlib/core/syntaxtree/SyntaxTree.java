package org.jaqlib.core.syntaxtree;

import org.jaqlib.core.ElementPredicate;
import org.jaqlib.util.Assert;

/**
 * Represents a simple syntax tree with AND and OR connections. The
 * {@link Connector} nodes always have a condition on their left leaves. An
 * additional condition is always stored at their right leaves. Only the last
 * {@link Connector} node has a condition at the right leave.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class SyntaxTree<T> implements ElementPredicate<T>
{

  private Root<T> root;
  private Connector<T> current;


  public boolean matches(T element)
  {
    if (root != null)
    {
      return root.visit(element);
    }
    // no root present, so no conditions should be evaluated
    // --> return true
    return true;
  }


  private boolean hasRoot()
  {
    return root != null;
  }


  private void setRoot(SyntaxTreeNode<T> root)
  {
    Assert.state(this.root == null, "Root condition can be set only once.");

    this.root = new Root<>();
    this.current = this.root;
    this.current.setRight(root);
  }


  private Connector<T> addConnector(Connector<T> connector)
  {
    SyntaxTreeNode<T> right = current.getRight();
    current.setRight(connector);
    connector.setLeft(right);

    current = connector;
    return connector;
  }


  private void addConnector(Connector<T> connector, Condition<T> condition)
  {
    if (hasRoot())
    {
      addConnector(connector).setRight(condition);
    }
    else
    {
      setRoot(condition);
    }
  }


  public void and(Condition<T> condition)
  {
    addConnector(new And<>(), condition);
  }


  public void or(Condition<T> condition)
  {
    addConnector(new Or<>(), condition);
  }


  public String getLogString()
  {
    if (root != null)
    {
      StringBuilder sb = new StringBuilder();
      sb.append(" WHERE");
      root.appendLogString(sb);
      return sb.toString();
    }
    else
    {
      return "";
    }
  }


}
