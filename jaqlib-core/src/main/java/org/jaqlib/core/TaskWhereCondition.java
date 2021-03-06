package org.jaqlib.core;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class TaskWhereCondition<T> implements WhereCondition<T>
{

  private final Task<? super T> task;


  public TaskWhereCondition(Task<? super T> task)
  {
    this.task = Assert.notNull(task);
  }


  public boolean evaluate(T element)
  {
    task.execute(element);
    return true;
  }

}
