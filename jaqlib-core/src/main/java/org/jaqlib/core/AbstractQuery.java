package org.jaqlib.core;

import org.jaqlib.core.reflect.MethodCallRecorder;
import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.core.syntaxtree.Condition;
import org.jaqlib.core.syntaxtree.SyntaxTree;
import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;
import org.jaqlib.util.LogUtil;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

  protected final SyntaxTree<T> tree = new SyntaxTree<>();
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


  protected void setDataSource(DataSourceType dataSource)
  {
    this.dataSource = dataSource;
  }


  @Override
  public QueryResult<T, DataSourceType> createQueryResult()
  {
    return new QueryResult<>(this);
  }


  private <R> SimpleWhereCondition<T, DataSourceType, R> createSimpleWhereCondition()
  {
    return new SimpleWhereCondition<>(this);
  }


  @Override
  public <R> SingleElementWhereCondition<T, DataSourceType, R> addElementAndWhereCondition()
  {
    SimpleWhereCondition<T, DataSourceType, R> condition = createSimpleWhereCondition();
    addAndWhereCondition(condition);
    return condition;
  }


  @Override
  public <R> SingleElementWhereCondition<T, DataSourceType, R> addElementOrWhereCondition()
  {
    SimpleWhereCondition<T, DataSourceType, R> condition = createSimpleWhereCondition();
    addOrWhereCondition(condition);
    return condition;
  }


  @Override
  public QueryResult<T, DataSourceType> addAndWhereCondition(
      WhereCondition<? super T> condition)
  {
    tree.and(new Condition<>(condition));
    return createQueryResult();
  }


  @Override
  public QueryResult<T, DataSourceType> addOrWhereCondition(
      WhereCondition<? super T> condition)
  {
    tree.or(new Condition<>(condition));
    return createQueryResult();
  }


  @Override
  public <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveAndWhereCondition()
  {
    ReflectiveWhereCondition<T, DataSourceType, R> condition = new ReflectiveWhereCondition<>(
        this, methodCallRecorder);
    addAndWhereCondition(condition);
    return condition;
  }


  @Override
  public <R> ReflectiveWhereCondition<T, DataSourceType, R> addReflectiveOrWhereCondition()
  {
    ReflectiveWhereCondition<T, DataSourceType, R> condition = new ReflectiveWhereCondition<>(
        this, methodCallRecorder);
    addOrWhereCondition(condition);
    return condition;
  }


  @Override
  public QueryResult<T, DataSourceType> addTask(Task<? super T> task)
  {
    TaskWhereCondition<T> condition = new TaskWhereCondition<>(task);
    addAndWhereCondition(condition);
    return createQueryResult();
  }


  @Override
  public void addTaskAndExecute(Task<? super T> task)
  {
    addTask(task);

    logQuery("ExecuteTask");
    addResults(new ArrayList<>(), false);
  }


  private String getCommaSeparatedString(Iterable<T> iterable)
  {
    return CollectionUtil.toString(iterable, ", ");
  }


  @Override
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


  @Override
  public T getFirstResult()
  {
    logQuery("FirstResult");

    List<T> result = new ArrayList<>();
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


  @Override
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


  @Override
  public List<T> getListResult()
  {
    logQuery("List");

    List<T> result = new ArrayList<>();
    addResults(result, false);
    return result;
  }


  @Override
  public Set<T> getSetResult()
  {
    logQuery("Set");

    Set<T> result = new HashSet<>();
    addResults(result, false);
    return result;
  }


  @Override
  public Vector<T> getVectorResult()
  {
    logQuery("Vector");

    Vector<T> result = new Vector<>();
    addResults(result, false);
    return result;
  }


  @Override
  public <KeyType> Map<KeyType, T> getMapResult(KeyType key)
  {
    logQuery("Map");

    Map<KeyType, T> result = new HashMap<>();
    addResults(result);
    return result;
  }


  @Override
  public <KeyType> Hashtable<KeyType, T> getHashtableResult(KeyType key)
  {
    logQuery("Hashtable");

    Hashtable<KeyType, T> result = new Hashtable<>();
    addResults(result);
    return result;
  }


  @Override
  public int count()
  {
    return getListResult().size();
  }


  @Override
  public int countDistinct()
  {
    return getSetResult().size();
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
