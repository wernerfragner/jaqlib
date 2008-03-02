package org.jaqlib.query.syntaxtree;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class SyntaxTree<T>
{

  private SyntaxTreeItem<T> root;
  private SyntaxTreeItem<T> current;


  public boolean visit(T item)
  {
    if (root != null)
    {
      return root.visit(item);
    }
    // no root present, so no conditions should be evaluated
    // --> return true
    return true;
  }


  public void setRoot(SyntaxTreeItem<T> root)
  {
    Assert.state(this.root == null, "Root condition can be set only once.");
    this.root = root;
    this.current = root;
  }


  public Connector<T> addConnector(Connector<T> connector)
  {
    connector.setLeft(current);
    current = connector;
    return connector;
  }


}
