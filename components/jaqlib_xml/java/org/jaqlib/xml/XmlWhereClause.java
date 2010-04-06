package org.jaqlib.xml;

import org.jaqlib.core.QueryResult;
import org.jaqlib.core.WhereClause;

public class XmlWhereClause<T> extends WhereClause<T, XmlSelectDataSource>
{

  public XmlWhereClause(XmlQuery<T> query)
  {
    super(query);
  }


  public QueryResult<T, XmlSelectDataSource> where(String xPath)
  {
    return getXmlQuery().createQueryResult(xPath);
  }


  private XmlQuery<T> getXmlQuery()
  {
    return (XmlQuery<T>) getQuery();
  }

}
