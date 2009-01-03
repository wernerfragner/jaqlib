package org.jaqlib.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaqlib.core.reflect.MethodCallRecorder;
import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.core.syntaxtree.And;
import org.jaqlib.core.syntaxtree.Condition;
import org.jaqlib.core.syntaxtree.Or;
import org.jaqlib.core.syntaxtree.SyntaxTree;
import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;
import org.jaqlib.util.LogUtil;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 * @param <DataSourceType>
 */
public abstract class AbstractQuery<T, DataSourceType> implements
    Query<T, DataSourceType>
{

  private final Logger log = LogUtil.getLogger(this);

  protected final SyntaxTree<T> tree = new SyntaxTree<T>();
  private final MethodCallRecorder methodCallRecorder;
  private DataSourceType dataSource;


  public AbstractQuery(MethodCallRecorder methodCallRecorder)
  {
    this.methodCallRecorder = Assert.notNull(methodCallRecorder);
  }


  protected MethodInvocation getCurrentInvocation()
  {
    return this.methodCallRecorder.getCurrentInvocation();
  }


  protected DataSourceType getDataSource()
  {
    return dataSource;
  }


  private <R> void setRoot(WhereCondition<T> condition)
  {
    tree.setRoot(new Condition<T>(condition));
  }


  public WhereClause<T, DataSourceType> createWhereClause(
      DataSourceType dataSource)
  {
    this.dataSource = dataSource;
    return new WhereClause<T, DataSourceType>(this);
  }


  public QueryResult<T, DataSourceType> createQueryResult()
  {
    return new QueryResult<T, DataSourceType>(this);
  }


  private <R> SimpleWhereCondition<T, DataSourceType, R> createSimpleWhereCondition()
  {
    return new SimpleWhereCondition<T, DataSourceType, R>(this);
  }


  public <R> SingleElementWhereCondition<T, DataSourceType, R> addSimpleWhereCondition()
  {
    SimpleWhereCondition<T, DataSourceType, R> condition = createSimpleWhereCondition();
    setRoot(condition);
    return condition;
  }


  public <R> SingleElementWhereCondition<T, DataSourceType, R> addSimpleAndWhereCondition()
  {
    SimpleWhereCondition<T, DataSourceType, R> condition = createSimpleWhereCondition();
    addAndWhereCondition(condition);
    return condition;
  }


  public <R> SingleElementWhereCondition<T, DataSourceType, R> addSimpleOrWhereCondition()
  {
    SimpleWhereCondition<T, DataSourceType, R> condition = createSimpleWhereCondition();
    addOrWhereCondition(condition);
    return condition;
  }


  public QueryResult<T, DataSourceType> addWhereCondition(
      WhereCondition<T> condition)
  {
    setRoot(condition);
    return createQueryResult();
  }


  public QueryResult<T, DataSourceType> addAndWhereCondition(
      WhereCondition<T> condition)
  {
    tree.addConnector(new And<T>()).setRight(new Condition<T>(condition));
    return createQueryResult();
  }


  public QueryResult<T, DataSourceType> addOrWhereCondition(
      WhereCondition<T> condition)
  {
    tree.addConnector(new Or<T>()).setRight(new Condition<T>(condition));
    return createQueryResult();
  }


  public <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveWhereCondition()
  {
    ReflectiveWhereCondition<T, DataSourceType, R> condition = new ReflectiveWhereCondition<T, DataSourceType, R>(
        this, methodCallRecorder);
    setRoot(condition);
    return condition;
  }


  public <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveAndWhereCondition()
  {
    ReflectiveWhereCondition<T, DataSourceType, R> condition = new ReflectiveWhereCondition<T, DataSourceType, R>(
        this, methodCallRecorder);
    addAndWhereCondition(condition);
    return condition;
  }


  public <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveOrWhereCondition()
  {
    ReflectiveWhereCondition<T, DataSourceType, R> condition = new ReflectiveWhereCondition<T, DataSourceType, R>(
        this, methodCallRecorder);
    addOrWhereCondition(condition);
    return condition;
  }


  public QueryResult<T, DataSourceType> addTask(Task<T> task)
  {
    TaskWhereCondition<T> condition = new TaskWhereCondition<T>(task);
    if (!tree.hasRoot())
    {
      setRoot(condition);
    }
    else
    {
      addAndWhereCondition(condition);
    }
    return createQueryResult();
  }


  public void addTaskAndExecute(Task<T> task)
  {
    addTask(task);

    logQuery("ExecuteTask");
    addResults(new ArrayList<T>(), false);
  }


  private String getCommaSeparatedString(Iterable<T> iterable)
  {
    return CollectionUtil.toString(iterable, ", ");
  }


  public T getUniqueResult()
  {
    logQuery("UniqueResult");

    final Set<T> setResult = getSetResult();
    if (setResult.isEmpty())
    {
      // no result found
      return null;
    }

    // check if only one result has been found
    if (setResult.size() > 1)
    {
      throw new QueryResultException(
          "There exist multiple results but only one is allowed. Results: "
              + getCommaSeparatedString(setResult));
    }
    return setResult.iterator().next();
  }


  public T getFirstResult()
  {
    logQuery("FirstResult");

    List<T> result = new ArrayList<T>();
    addResults(result, true);
    if (result.size() > 0)
    {
      return result.get(0);
    }
    else
    {
      return null;
    }
  }


  public T getLastResult()
  {
    logQuery("LastResult");

    List<T> result = getListResult();
    if (result.size() > 0)
    {
      return result.get(result.size() - 1);
    }
    else
    {
      return null;
    }
  }


  public List<T> getListResult()
  {
    logQuery("List");

    List<T> result = new ArrayList<T>();
    addResults(result, false);
    return result;
  }


  public Set<T> getSetResult()
  {
    logQuery("Set");

    Set<T> result = new HashSet<T>();
    addResults(result, false);
    return result;
  }


  public Vector<T> getVectorResult()
  {
    logQuery("Vector");

    Vector<T> result = new Vector<T>();
    addResults(result, false);
    return result;
  }


  public <KeyType> Map<KeyType, T> getMapResult(KeyType key)
  {
    logQuery("Map");

    Map<KeyType, T> result = new HashMap<KeyType, T>();
    addResults(result);
    return result;
  }


  public <KeyType> Hashtable<KeyType, T> getHashtableResult(KeyType key)
  {
    logQuery("Hashtable");

    Hashtable<KeyType, T> result = new Hashtable<KeyType, T>();
    addResults(result);
    return result;
  }


  protected Object getKey(T element, MethodInvocation invocation)
  {
    return invocation.invoke(element);
  }


  protected abstract void addResults(Collection<T> result,
      boolean stopAtFirstMatch);


  protected abstract <KeyType> void addResults(final Map<KeyType, T> resultMap);


  private void logQuery(String resultType)
  {
    if (log.isLoggable(Level.FINER))
    {
      log.finer("SELECT " + getResultDefinitionString() + " FROM "
          + getDataSource() + tree.getLogString() + " AS " + resultType);
    }
  }


  protected abstract String getResultDefinitionString();

}
