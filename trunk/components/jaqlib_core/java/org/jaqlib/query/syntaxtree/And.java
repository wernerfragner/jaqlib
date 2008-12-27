package org.jaqlib.query.syntaxtree;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class And<T> extends Connector<T>
{

  public boolean visit(T element)
  {
    return getLeft().visit(element) && getRight().visit(element);
  }

}
