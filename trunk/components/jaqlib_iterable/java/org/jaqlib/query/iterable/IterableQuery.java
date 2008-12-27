package org.jaqlib.query.iterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jaqlib.query.FromClause;
import org.jaqlib.query.Query;
import org.jaqlib.query.QueryResult;
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.SimpleWhereCondition;
import org.jaqlib.query.SingleElementWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.query.syntaxtree.And;
import org.jaqlib.query.syntaxtree.Condition;
import org.jaqlib.query.syntaxtree.Or;
import org.jaqlib.query.syntaxtree.SyntaxTree;
import org.jaqlib.reflect.MethodCallRecorder;
import org.jaqlib.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T> the result element class of the query.
 */
public class IterableQuery<T> implements Query<T, Iterable<T>>
{

  private final SyntaxTree<T> tree = new SyntaxTree<T>();
  private final MethodCallRecorder invocationRecorder;

  private Iterable<T> dataSource;


  public IterableQuery(MethodCallRecorder invocationRecorder)
  {
    this.invocationRecorder = Assert.notNull(invocationRecorder);
  }


  private MethodInvocation getLastInvocation()
  {
    return this.invocationRecorder.getLastInvocation();
  }


  /**
   * The element class is not used because IterableQuery only supports one
   * element class.
   */
  public FromClause<T, Iterable<T>> createFromClause(Class<T> resultElementClass)
  {
    return new FromClause<T, Iterable<T>>(this);
  }


  public FromClause<T, Iterable<T>> createFromClause(
      Class<T>... resultElementClasses)
  {
    Class<T> resultElementClass = null;
    if (resultElementClasses != null && resultElementClasses.length > 0)
    {
      Assert.size(1, resultElementClasses,
          "Only one result element class is supported.");
      resultElementClass = resultElementClasses[0];
    }
    return createFromClause(resultElementClass);
  }


  public WhereClause<T, Iterable<T>> createWhereClause(Iterable<T> dataSource)
  {
    this.dataSource = dataSource;
    return new WhereClause<T, Iterable<T>>(this);
  }


  public QueryResult<T, Iterable<T>> createQueryResult()
  {
    return new QueryResult<T, Iterable<T>>(this);
  }


  public QueryResult<T, Iterable<T>> addWhereCondition(
      WhereCondition<T> condition)
  {
    tree.setRoot(new Condition<T>(condition));
    return createQueryResult();
  }


  public QueryResult<T, Iterable<T>> addAndWhereCondition(
      WhereCondition<T> condition)
  {
    tree.addConnector(new And<T>()).setRight(new Condition<T>(condition));
    return createQueryResult();
  }


  public QueryResult<T, Iterable<T>> addOrWhereCondition(
      WhereCondition<T> condition)
  {
    tree.addConnector(new Or<T>()).setRight(new Condition<T>(condition));
    return createQueryResult();
  }


  public <R> SingleElementWhereCondition<T, Iterable<T>, R> addSimpleWhereCondition()
  {
    SimpleWhereCondition<T, Iterable<T>, R> condition = new SimpleWhereCondition<T, Iterable<T>, R>(
        this);
    tree.setRoot(new Condition<T>(condition));
    return condition;
  }


  public <R> ReflectiveWhereCondition<T, Iterable<T>, R> addReflectiveWhereCondition()
  {
    ReflectiveWhereCondition<T, Iterable<T>, R> condition = new ReflectiveWhereCondition<T, Iterable<T>, R>(
        this, invocationRecorder);
    tree.setRoot(new Condition<T>(condition));
    return condition;
  }


  public <R> ReflectiveWhereCondition<T, Iterable<T>, R> addReflectiveAndWhereCondition()
  {
    ReflectiveWhereCondition<T, Iterable<T>, R> condition = new ReflectiveWhereCondition<T, Iterable<T>, R>(
        this, invocationRecorder);
    addAndWhereCondition(condition);
    return condition;
  }


  public <R> ReflectiveWhereCondition<T, Iterable<T>, R> addReflectiveOrWhereCondition()
  {
    ReflectiveWhereCondition<T, Iterable<T>, R> condition = new ReflectiveWhereCondition<T, Iterable<T>, R>(
        this, invocationRecorder);
    addOrWhereCondition(condition);
    return condition;
  }


  public List<T> getListResult()
  {
    return addResults(new ArrayList<T>());
  }


  public Vector<T> getVectorResult()
  {
    return addResults(new Vector<T>());
  }


  public Set<T> getSetResult()
  {
    return addResults(new HashSet<T>());
  }


  private <CollType extends Collection<T>> CollType addResults(CollType result)
  {
    for (T element : dataSource)
    {
      if (tree.visit(element))
      {
        result.add(element);
      }
    }
    return result;
  }


  public T getFirstResult()
  {
    for (T element : dataSource)
    {
      if (tree.visit(element))
      {
        return element;
      }
    }
    return null;
  }


  public T getLastResult()
  {
    T foundElement = null;
    for (T element : dataSource)
    {
      if (tree.visit(element))
      {
        foundElement = element;
      }
    }
    return foundElement;
  }


  public T getUniqueResult()
  {
    final Set<T> setResult = getSetResult();
    if (setResult.isEmpty())
    {
      // no result found
      return null;
    }

    // check if only one result has been found
    Assert.size(1, setResult,
        "There exists multiple results but only one is allowed.");
    return setResult.iterator().next();
  }


  public <KeyType> Map<KeyType, T> getMapResult(KeyType key)
  {
    return addResults(new HashMap<KeyType, T>());
  }


  public <KeyType> Hashtable<KeyType, T> getHashtableResult(KeyType key)
  {
    return addResults(new Hashtable<KeyType, T>());
  }


  private <KeyType, CollType extends Map<KeyType, T>> CollType addResults(
      final CollType resultMap)
  {
    final MethodInvocation invocation = getLastInvocation();
    for (T element : dataSource)
    {
      if (element != null && tree.visit(element))
      {
        final KeyType elementKey = (KeyType) getKey(element, invocation);
        resultMap.put(elementKey, element);
      }
    }
    return resultMap;
  }


  private Object getKey(T element, MethodInvocation invocation)
  {
    return invocation.invoke(element);
  }


}
