package org.jaqlib.query.syntaxtree;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface SyntaxTreeItem<T>
{

  boolean visit(T item);

}
