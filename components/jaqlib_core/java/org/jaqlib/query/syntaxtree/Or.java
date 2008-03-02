package org.jaqlib.query.syntaxtree;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class Or<T> extends Connector<T>
{

  @Override
  public boolean visit(T item)
  {
    return getLeft().visit(item) || getRight().visit(item);
  }

}
