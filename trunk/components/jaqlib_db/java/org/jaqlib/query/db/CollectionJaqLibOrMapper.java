package org.jaqlib.query.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class CollectionJaqLibOrMapper<T> extends AbstractJaqLibOrMapper<T>
{

  public void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    try
    {
      ResultSet rs = queryDatabase();
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

}
