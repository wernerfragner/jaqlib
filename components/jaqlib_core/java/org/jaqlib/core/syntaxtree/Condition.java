package org.jaqlib.core.syntaxtree;

import org.jaqlib.core.LoggableQueryItem;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.util.Assert;
import org.jaqlib.util.ReflectionUtil;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class Condition<T> implements SyntaxTreeNode<T>
{

  private final WhereCondition<? super T> condition;


  public Condition(WhereCondition<? super T> condition)
  {
    this.condition = Assert.notNull(condition);
  }


  public boolean visit(T element)
  {
    return condition.evaluate(element);
  }


  public void appendLogString(StringBuilder sb)
  {
    if (condition instanceof LoggableQueryItem)
    {
      // condition supports logging
      LoggableQueryItem qi = (LoggableQueryItem) condition;
      qi.appendLogString(sb);
    }
    else if (ReflectionUtil.hasDeclaredMethod(condition.getClass(), "toString"))
    {
      // WhereCondition does declare an 'own' toString() method; use this method
      sb.append(condition);
    }
    else
    {
      // no log information available, append default text
      sb.append("customWhereCondition");
    }
  }

}
