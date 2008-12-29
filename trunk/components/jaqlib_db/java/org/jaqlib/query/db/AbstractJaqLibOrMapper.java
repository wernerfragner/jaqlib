package org.jaqlib.query.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.jaqlib.db.BeanDbSelectResult;
import org.jaqlib.db.DbSelect;
import org.jaqlib.db.DbSelectResult;
import org.jaqlib.db.PrimitiveDbSelectResult;
import org.jaqlib.query.ElementPredicate;
import org.jaqlib.util.reflect.MethodInvocation;
import org.jaqlib.util.reflect.ReflectionUtil;

public class AbstractJaqLibOrMapper<T>
{

  private static final Object NO_RESULT = new Object();

  private static final Logger LOG = Logger
      .getLogger(AbstractJaqLibOrMapper.class.getName());

  private DbSelect dataSource;
  private ElementPredicate<T> predicate;
  private DbSelectResult<T> resultDefinition;
  private MethodInvocation methodInvocation;
  private boolean strictColumnCheck = false;


  public void setStrictColumnCheck(boolean strictColumnCheck)
  {
    this.strictColumnCheck = strictColumnCheck;
  }


  public void setDataSource(DbSelect dataSource)
  {
    this.dataSource = dataSource;
  }


  protected DbSelect getDataSource()
  {
    return dataSource;
  }


  public void setElementPredicate(ElementPredicate<T> predicate)
  {
    this.predicate = predicate;
  }


  public void setResultDefinition(DbSelectResult<T> result)
  {
    this.resultDefinition = result;
  }


  public void setMethodInvocation(MethodInvocation methodInvocation)
  {
    this.methodInvocation = methodInvocation;
  }


  protected boolean shouldAddToResult(T element)
  {
    return predicate.matches(element);
  }


  protected T extractElement(ResultSet rs) throws SQLException
  {
    if (resultDefinition instanceof PrimitiveDbSelectResult)
    {
      return extractSingleResult(rs, (PrimitiveDbSelectResult<T>) resultDefinition);
    }
    else if (resultDefinition instanceof BeanDbSelectResult)
    {
      return extractMultiResult(rs, (BeanDbSelectResult<T>) resultDefinition);
    }
    else
    {
      final String resultClassName = (resultDefinition == null) ? "[null]"
          : resultDefinition.getClass().getName();
      throw new IllegalStateException("Unsupported DbSelectResultType: "
          + resultClassName);
    }
  }


  private T extractElement(ResultSet rs, DbSelectResult<?> result)
      throws SQLException
  {
    if (result instanceof PrimitiveDbSelectResult)
    {
      return extractSingleResult(rs, (PrimitiveDbSelectResult) result);
    }
    else if (result instanceof BeanDbSelectResult)
    {
      return extractMultiResult(rs, (BeanDbSelectResult) result);
    }
    else
    {
      final String resultClassName = (result == null) ? "[null]" : result
          .getClass().getName();
      throw new IllegalStateException("Unsupported DbSelectResultType: "
          + resultClassName);
    }
  }


  private T extractMultiResult(ResultSet rs,
      BeanDbSelectResult<T> complexResult) throws SQLException
  {
    T bean = complexResult.newBeanInstance();
    for (DbSelectResult<?> selectResult : complexResult)
    {
      Object value = extractElement(rs, selectResult);
      if (value != NO_RESULT)
      {
        setValue(bean, selectResult.getFieldName(), value);
      }
    }
    return bean;
  }


  private void setValue(T bean, String fieldName, Object value)
  {
    ReflectionUtil.setFieldValue(bean, fieldName, value);
  }


  @SuppressWarnings("unchecked")
  private T extractSingleResult(ResultSet rs,
      PrimitiveDbSelectResult<T> singleResult) throws SQLException
  {
    return (T) extractObject(rs, singleResult);
  }


  private Object extractObject(ResultSet rs,
      PrimitiveDbSelectResult<?> singleResult) throws SQLException
  {
    if (singleResult.hasColumnIndex())
    {
      return rs.getObject(singleResult.getColumnIndex());
    }
    else if (singleResult.hasColumnName())
    {
      final String columnName = singleResult.getColumnName();
      if (strictColumnCheck || getDataSource().hasColumn(columnName))
      {
        return rs.getObject(columnName);
      }
      else
      {
        LOG.info("SELECT statement does not contain colum '" + columnName
            + "'. Column is ignored.");
        return NO_RESULT;
      }
    }
    else
    {
      throw new IllegalStateException(
          "Result must have a column index or a colum name.");
    }
  }


  protected void handleSqlException(SQLException sqle)
  {
    QueryDataSourceException e = new QueryDataSourceException(sqle);
    e.setStackTrace(sqle.getStackTrace());
    throw e;
  }


  protected ResultSet queryDatabase() throws SQLException
  {
    return dataSource.execute();
  }


  @SuppressWarnings("unchecked")
  protected <KeyType> KeyType getKey(T element)
  {
    return (KeyType) methodInvocation.invoke(element);
  }

}
