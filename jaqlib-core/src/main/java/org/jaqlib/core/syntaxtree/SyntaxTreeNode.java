package org.jaqlib.core.syntaxtree;

import org.jaqlib.core.LoggableQueryItem;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public interface SyntaxTreeNode<T> extends LoggableQueryItem
{

  boolean visit(T element);

}
