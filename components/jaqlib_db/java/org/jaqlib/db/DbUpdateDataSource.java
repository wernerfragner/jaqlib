package org.jaqlib.db;

import javax.sql.DataSource;

/**
 * @author Werner Fragner
 */
public class DbUpdateDataSource extends AbstractDbDmlDataSource
{

  private final String whereClause;


  public DbUpdateDataSource(DataSource dataSource, String tableName,
      String whereClause)
  {
    super(dataSource, tableName);
    this.whereClause = whereClause;
  }


  @Override
  protected <T> String buildSql(BeanMapping<T> beanMapping)
  {
    final StringBuilder updateSql = new StringBuilder();
    updateSql.append("UPDATE ");
    updateSql.append(tableName);
    updateSql.append(" SET ");

    boolean first = true;

    for (ColumnMapping<?> mapping : beanMapping)
    {
      if (!first)
      {
        updateSql.append(", ");
      }
      else
      {
        first = false;
      }

      mapping.appendColumn(updateSql);
    }

    appendWhereClause(updateSql, whereClause);

    return updateSql.toString();
  }

}
