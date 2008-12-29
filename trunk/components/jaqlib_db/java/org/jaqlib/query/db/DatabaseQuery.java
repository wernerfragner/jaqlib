package org.jaqlib.query.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.jaqlib.db.DbSelect;
import org.jaqlib.db.DbSelectResult;
import org.jaqlib.db.ComplexDbSelectResult;
import org.jaqlib.db.SingleDbSelectResult;
import org.jaqlib.query.AbstractQuery;
import org.jaqlib.query.FromClause;
import org.jaqlib.reflect.MethodCallRecorder;
import org.jaqlib.reflect.MethodInvocation;
import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class DatabaseQuery<T> extends AbstractQuery<T, DbSelect>
{

  private DbSelectResult<T> result;


  public DatabaseQuery(MethodCallRecorder methodCallRecorder)
  {
    super(methodCallRecorder);
  }


  public FromClause<T, DbSelect> createFromClause(DbSelectResult<T> result)
  {
    this.result = Assert.notNull(result);
    return new FromClause<T, DbSelect>(this);
  }


  private T extractElement(ResultSet rs) throws SQLException
  {
    if (result instanceof SingleDbSelectResult)
    {
      return extractSingleResult(rs, (SingleDbSelectResult<T>) result);
    }
    else if (result instanceof ComplexDbSelectResult)
    {
      return extractMultiResult(rs, (ComplexDbSelectResult<T>) result);
    }
    else
    {
      String resultClassName = (result == null) ? "[null]" : result.getClass()
          .getName();
      throw new IllegalStateException("Unsupported DbSelectResultType: "
          + resultClassName);
    }
  }


  private T extractMultiResult(ResultSet rs, ComplexDbSelectResult<T> result2)
  {
    // TODO FRAW
    return null;
  }


  private T extractSingleResult(ResultSet rs,
      SingleDbSelectResult<T> singleResult) throws SQLException
  {
    if (singleResult.hasIndex())
    {
      return (T) rs.getObject(singleResult.getIndex());
    }
    else if (singleResult.hasName())
    {
      return (T) rs.getObject(singleResult.getName());
    }
    else
    {
      throw new IllegalStateException(
          "Result must have a column index or a colum name.");
    }
  }


  private void handleSqlException(SQLException sqle)
  {
    throw new QueryDataSourceException(sqle);
  }


  private ResultSet queryDatabase() throws SQLException
  {
    return getDataSource().execute();
  }


  @Override
  protected void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    try
    {
      ResultSet rs = queryDatabase();
      while (rs.next())
      {
        T element = extractElement(rs);
        if (tree.visit(element))
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
    final MethodInvocation invocation = getLastInvocation();
    try
    {
      ResultSet rs = queryDatabase();
      while (rs.next())
      {
        T element = extractElement(rs);
        if (element != null && tree.visit(element))
        {
          final KeyType elementKey = getKey(element, invocation);
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


}
