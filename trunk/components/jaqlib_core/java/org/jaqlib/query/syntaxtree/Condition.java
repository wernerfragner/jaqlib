package org.jaqlib.query.syntaxtree;

import org.jaqlib.query.WhereCondition;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class Condition<T> implements SyntaxTreeItem<T>
{

  private final WhereCondition<T> condition;


  public Condition(WhereCondition<T> condition)
  {
    this.condition = Assert.notNull(condition);
  }


  @Override
  public boolean visit(T item)
  {
    return condition.evaluate(item);
  }

}
