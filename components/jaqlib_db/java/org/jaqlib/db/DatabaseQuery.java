package org.jaqlib.db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.jaqlib.query.AbstractQuery;
import org.jaqlib.query.FromClause;
import org.jaqlib.reflect.MethodCallRecorder;
import org.jaqlib.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T> the result element class of the query.
 */
public class DatabaseQuery<T> extends AbstractQuery<T, DbSelectDataSource>
{

  private AbstractMapping<T> mapping;


  public DatabaseQuery(MethodCallRecorder methodCallRecorder)
  {
    super(methodCallRecorder);
  }


  public FromClause<T, DbSelectDataSource> createFromClause(
      AbstractMapping<T> mapping)
  {
    this.mapping = Assert.notNull(mapping);
    return new FromClause<T, DbSelectDataSource>(this);
  }


  @Override
  protected void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    try
    {
      DbResultSet rs = queryDatabase();
      while (rs.next())
      {
        T element = extractElement(rs);
        if (shouldAddToResult(element))
        {
          result.add(element);

          if (stopAtFirstMatch)
          {
            return;
          }
        }
      }
    }
    catch (SQLException sqle)
    {
      handleSqlException(sqle);
    }
    finally
    {
      getDataSource().close();
    }
  }


  @Override
  protected <KeyType> void addResults(final Map<KeyType, T> resultMap)
  {
    final MethodInvocation invocation = getCurrentInvocation();

    try
    {
      DbResultSet rs = queryDatabase();
      while (rs.next())
      {
        T element = extractElement(rs);
        if (element != null && shouldAddToResult(element))
        {
          @SuppressWarnings("unchecked")
          final KeyType elementKey = (KeyType) getKey(element, invocation);
          resultMap.put(elementKey, element);
        }
      }
    }
    catch (SQLException sqle)
    {
      handleSqlException(sqle);
    }
    finally
    {
      getDataSource().close();
    }
  }


  private boolean shouldAddToResult(T element)
  {
    return tree.matches(element);
  }


  private T extractElement(DbResultSet rs) throws SQLException
  {
    return mapping.getValue(rs);
  }


  private void handleSqlException(SQLException sqle)
  {
    QueryDataSourceException e = new QueryDataSourceException(sqle);
    e.setStackTrace(sqle.getStackTrace());
    throw e;
  }


  private DbResultSet queryDatabase() throws SQLException
  {
    return getDataSource().execute();
  }

}
