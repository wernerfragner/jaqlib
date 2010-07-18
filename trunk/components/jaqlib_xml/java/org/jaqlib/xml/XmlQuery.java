package org.jaqlib.xml;

import org.jaqlib.core.DataSourceQuery;
import org.jaqlib.core.QueryResult;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.reflect.MethodCallRecorder;

/**
 * Central class representing the XML query. It holds information about the XML
 * query. This class is for internal use only.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the result type of the query.
 */
public class XmlQuery<T> extends DataSourceQuery<T, XmlSelectDataSource>
{

  public XmlQuery(MethodCallRecorder methodCallRecorder, BeanMapping<T> mapping)
  {
    super(methodCallRecorder, mapping);
  }


  public XmlFromClause<T> createFromClause()
  {
    return new XmlFromClause<T>(this);
  }


  public XmlWhereClause<T> createWhereClause(XmlSelectDataSource dataSource)
  {
    super.setDataSource(dataSource);
    return new XmlWhereClause<T>(this);
  }


  public QueryResult<T, XmlSelectDataSource> createQueryResult(String xPathExpression)
  {
    getDataSource().setXPathExpression(xPathExpression);
    return super.createQueryResult();
  }

}
