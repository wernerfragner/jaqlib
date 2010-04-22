package org.jaqlib.xml;

import org.jaqlib.util.FileResource;
import org.jaqlib.util.Resource;


public class XmlFromClause<T>
{

  private final XmlQuery<T> query;


  public XmlFromClause(XmlQuery<T> query)
  {
    this.query = query;
  }


  public XmlWhereClause<T> from(String xmlPath)
  {
    return from(new FileResource(xmlPath));
  }


  public XmlWhereClause<T> from(Resource xmlPath)
  {
    return fromAttributes(xmlPath);
  }


  public XmlWhereClause<T> from(XmlSelectDataSource ds)
  {
    return query.createWhereClause(ds);
  }


  public XmlWhereClause<T> fromAttributes(String xmlPath)
  {
    return fromAttributes(new FileResource(xmlPath));
  }


  public XmlWhereClause<T> fromAttributes(Resource xmlPath)
  {
    return from(new XmlSelectDataSource(xmlPath, true));
  }


  public XmlWhereClause<T> fromAttributes(XmlSelectDataSource dataSource)
  {
    dataSource.setUseAttributes(true);
    return from(dataSource);
  }


  public XmlWhereClause<T> fromElements(String xmlPath)
  {
    return fromElements(new FileResource(xmlPath));
  }


  public XmlWhereClause<T> fromElements(Resource xmlPath)
  {
    return from(new XmlSelectDataSource(xmlPath, false));
  }


  public XmlWhereClause<T> fromElements(XmlSelectDataSource dataSource)
  {
    dataSource.setUseAttributes(false);
    return from(dataSource);
  }

}
