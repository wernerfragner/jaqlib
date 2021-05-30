package org.jaqlib.db;

import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.FieldMapping;

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
    for (FieldMapping<?> mapping : beanMapping)
    {
      ColumnMapping<?> cMapping = cast(mapping);
      if (!first)
      {
        columns.append(", ");
        values.append(", ");
      }
      else
      {
        first = false;
      }

      cMapping.appendColumn(columns, values);
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
