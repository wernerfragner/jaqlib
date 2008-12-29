package org.jaqlib.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class ComplexDbSelectResult<T> extends DbSelectResult<T> implements
    Iterable<DbSelectResult<?>>
{

  public List<DbSelectResult<?>> results = new ArrayList<DbSelectResult<?>>();


  public void addResult(DbSelectResult<?> result)
  {
    Assert.notNull(result);
    results.add(result);
  }


  public Iterator<DbSelectResult<?>> iterator()
  {
    return results.iterator();
  }


}
