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


  public void setRoot(SyntaxTreeNode<T> root)
  {
    Assert.state(this.root == null, "Root condition can be set only once.");

    this.root = new Root<T>();
    this.current = this.root;
    this.current.setRight(root);
  }


  public Connector<T> addConnector(Connector<T> connector)
  {
    SyntaxTreeNode<T> right = current.getRight();
    current.setRight(connector);
    connector.setLeft(right);

    current = connector;
    return connector;
  }

}
