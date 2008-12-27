package org.jaqlib.query.syntaxtree;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface SyntaxTreeNode<T>
{

  boolean visit(T element);

}
