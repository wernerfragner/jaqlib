package org.jaqlib.db;

public class UpdateUsing<T> extends Using<T>
{

  public UpdateUsing(T bean, DbUpdateDataSource dataSource)
  {
    super(bean, dataSource);
  }


  /**
   * Adds a custom WHERE clause to the UPDATE statement. <b>NOTE: using this
   * method overwrites the previously set WHERE clause in
   * {@link DbUpdateDataSource}!</b>.
   * 
   * @param whereClause the WHERE clause that should be applied to the UPDATE
   *          statement.
   * @return a using clause for defining the DB column to object field mapping.
   */
  public Using<T> where(String whereClause)
  {
    getUpdateDataSource().setWhereClause(whereClause);
    return new UpdateUsing<T>(bean, getUpdateDataSource());
  }


  private DbUpdateDataSource getUpdateDataSource()
  {
    return (DbUpdateDataSource) dataSource;
  }

}
