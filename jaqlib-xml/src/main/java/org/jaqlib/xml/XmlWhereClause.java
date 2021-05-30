package org.jaqlib.xml;

import org.jaqlib.core.QueryResult;
import org.jaqlib.core.WhereClause;

/**
 * Represents the WHERE part of the XML query. It provides methods for
 * specifying the XPath expression.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the result type of the query.
 */
public class XmlWhereClause<T> extends WhereClause<T, XmlSelectDataSource>
{

  /**
   * Default constructor.
   * 
   * @param query
   */
  public XmlWhereClause(XmlQuery<T> query)
  {
    super(query);
  }


  /**
   * Constrains the query using the given XPath expression.<br>
   * (see <a href="http ://www.w3.org/TR/xpath/">W3C XPath Language</a>)
   * 
   * @param xPathExpression a valid XPath expression (see <a href="http
   *          ://www.w3.org/TR/xpath/">W3C XPath Language</a>).
   * @return the result of the XML query.
   */
  public QueryResult<T, XmlSelectDataSource> where(String xPathExpression)
  {
    return getXmlQuery().createQueryResult(xPathExpression);
  }


  private XmlQuery<T> getXmlQuery()
  {
    return (XmlQuery<T>) getQuery();
  }

}
