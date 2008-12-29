package org.jaqlib.query.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MapJaqLibOrMapper<T> extends AbstractJaqLibOrMapper<T>
{

  public <KeyType> void addResults(final Map<KeyType, T> resultMap)
  {
    try
    {
      ResultSet rs = queryDatabase();
      while (rs.next())
      {
        T element = extractElement(rs);
        if (element != null && shouldAddToResult(element))
        {
          final KeyType elementKey = getKey(element);
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
