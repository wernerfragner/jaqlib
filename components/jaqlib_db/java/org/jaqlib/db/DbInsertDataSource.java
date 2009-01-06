package org.jaqlib.db;

import javax.sql.DataSource;

/**
 * @author Werner Fragner
 */
public class DbInsertDataSource extends AbstractDbDmlDataSource
{


  public DbInsertDataSource(DataSource dataSource, String tableName)
  {
    super(dataSource, tableName);
  }


  @Override
  protected <T> String buildSql(BeanMapping<T> beanMapping)
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
    return insertSql.toString();
  }

}
