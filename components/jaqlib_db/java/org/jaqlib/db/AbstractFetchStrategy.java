package org.jaqlib.db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.jaqlib.core.ElementPredicate;
import org.jaqlib.core.reflect.MethodInvocation;

/**
 * @author Werner Fragner
 * 
 * @param <T> the element type of the query.
 */
public abstract class AbstractFetchStrategy<T>
{

  private ElementPredicate<T> predicate;
  private DbSelectDataSource dataSource;
  private AbstractMapping<T> mapping;


  public void setPredicate(ElementPredicate<T> predicate)
  {
    this.predicate = predicate;
  }


  protected ElementPredicate<T> getPredicate()
  {
    return predicate;
  }


  public void setDataSource(DbSelectDataSource dataSource)
  {
    this.dataSource = dataSource;
  }


  public void setMapping(AbstractMapping<T> mapping)
  {
    this.mapping = mapping;
  }


  public void addResults(Collection<T> results)
  {
    try
    {
      final DbResultSet rs = queryDatabase();

      boolean stop = false;
      while (!stop && rs.next())
      {
        final T element = extractElement(rs);
        if (shouldAddToResult(element))
        {
          results.add(element);
          stop = recordProcessed(element, true);
        }
        else
        {
          stop = recordProcessed(element, false);
        }
      }
    }
    catch (SQLException sqle)
    {
      handleSqlException(sqle);
    }
    finally
    {
      getDataSource().closeAfterQuery();
    }
  }


  protected abstract boolean recordProcessed(T element, boolean isMatch);


  private boolean shouldAddToResult(T element)
  {
    return predicate.matches(element);
  }


  private T extractElement(DbResultSet rs) throws SQLException
  {
    return mapping.getValue(rs);
  }


  private void handleSqlException(SQLException sqle)
  {
    DataSourceQueryException e = new DataSourceQueryException(sqle);
    e.setStackTrace(sqle.getStackTrace());
    throw e;
  }


  private DbResultSet queryDatabase() throws SQLException
  {
    return getDataSource().execute();
  }


  private DbSelectDataSource getDataSource()
  {
    return dataSource;
  }


  public <KeyType> void addResults(Map<KeyType, T> results,
      MethodInvocation invocation)
  {
    try
    {
      final DbResultSet rs = queryDatabase();

      boolean stop = false;
      while (!stop && rs.next())
      {
        final T element = extractElement(rs);
        if (element != null && shouldAddToResult(element))
        {
          @SuppressWarnings("unchecked")
          final KeyType elementKey = (KeyType) getKey(element, invocation);
          results.put(elementKey, element);

          stop = recordProcessed(element, true);
        }
        else
        {
          stop = recordProcessed(element, false);
        }
      }
    }
    catch (SQLException sqle)
    {
      handleSqlException(sqle);
    }
    finally
    {
      getDataSource().closeAfterQuery();
    }
  }


  private Object getKey(T element, MethodInvocation invocation)
  {
    return invocation.invoke(element);
  }


}
