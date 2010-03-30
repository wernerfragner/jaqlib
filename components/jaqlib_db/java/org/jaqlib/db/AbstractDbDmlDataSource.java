package org.jaqlib.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public abstract class AbstractDbDmlDataSource extends AbstractDbDataSource
{

  protected final String tableName;


  public AbstractDbDmlDataSource(DataSource dataSource, String tableName)
  {
    super(dataSource);
    this.tableName = Assert.notNull(tableName);
  }


  public <T> int execute(T bean, BeanMapping<T> beanMapping)
  {
    final String sql = buildSql(beanMapping);

    log.fine("Executing SQL statement: " + sql);

    try
    {
      PreparedStatement stmt = getPreparedStatement(sql);

      int i = 1;
      for (ColumnMapping<?> mapping : beanMapping)
      {
        mapping.setValue(i, stmt, bean, beanMapping);
        i++;
      }

      stmt.execute();
      commit();
      return stmt.getUpdateCount();
    }
    catch (SQLException e)
    {
      throw handleSqlException(e);
    }
    finally
    {
      close();
    }
  }


  protected abstract <T> String buildSql(BeanMapping<T> beanMapping);


}