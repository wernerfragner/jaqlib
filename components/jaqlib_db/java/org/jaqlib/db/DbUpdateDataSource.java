package org.jaqlib.db;

import javax.sql.DataSource;

import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.ColumnMapping;

/**
 * @author Werner Fragner
 */
public class DbUpdateDataSource extends AbstractDbDmlDataSource
{

  private String whereClause;


  public DbUpdateDataSource(DataSource dataSource, String tableName,
      String whereClause)
  {
    super(dataSource, tableName);
    setWhereClause(whereClause);
  }


  public String getWhereClause()
  {
    return whereClause;
  }


  public void setWhereClause(String whereClause)
  {
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
