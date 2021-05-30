package org.jaqlib.core.syntaxtree;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class Or<T> extends Connector<T>
{

  public boolean visit(T element)
  {
    return getLeft().visit(element) || getRight().visit(element);
  }

}
