package org.jaqlib.core.syntaxtree;

import org.jaqlib.core.WhereCondition;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class Condition<T> implements SyntaxTreeNode<T>
{

  private final WhereCondition<T> condition;


  public Condition(WhereCondition<T> condition)
  {
    this.condition = Assert.notNull(condition);
  }


  public boolean visit(T element)
  {
    return condition.evaluate(element);
  }

}
