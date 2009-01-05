package org.jaqlib.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jaqlib.util.Assert;

public class DbInsertDataSource extends AbstractDbDataSource
{

  private final String tableName;


  public DbInsertDataSource(DataSource dataSource, String tableName)
  {
    super(dataSource);
    this.tableName = Assert.notNull(tableName);
  }


  public <T> void execute(T bean, BeanMapping<T> beanMapping)
  {
    final StringBuilder columns = new StringBuilder();
    final StringBuilder values = new StringBuilder();

    boolean first = true;
    for (AbstractMapping<?> mapping : beanMapping)
    {
      if (!first)
      {
        columns.append(", ");
        values.append(", ");
      }
      else
      {
        first = false;
      }

      mapping.appendColumn(columns, values);
    }

    final StringBuilder insertSql = new StringBuilder();
    insertSql.append("INSERT INTO ");
    insertSql.append(tableName);
    insertSql.append(" (");
    insertSql.append(columns);
    insertSql.append(") VALUES (");
    insertSql.append(values);
    insertSql.append(")");

    try
    {
      PreparedStatement stmt = getPreparedStatement(insertSql.toString());

      int i = 1;
      for (AbstractMapping<?> mapping : beanMapping)
      {
        mapping.setValue(i, stmt, bean, beanMapping);
        i++;
      }

      stmt.execute();
      commit();
    }
    catch (SQLException e)
    {
      handleSqlException(e);
    }
    finally
    {
      close();
    }
  }


  private void handleSqlException(SQLException sqle)
  {
    DataSourceQueryException e = new DataSourceQueryException(sqle);
    e.setStackTrace(sqle.getStackTrace());
    throw e;
  }

}
