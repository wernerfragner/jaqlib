package org.jaqlib.xml;

import org.jaqlib.util.FilePath;


public class XmlFromClause<T>
{

  private final XmlQuery<T> query;


  public XmlFromClause(XmlQuery<T> query)
  {
    this.query = query;
  }


  public XmlWhereClause<T> from(XmlSelectDataSource ds)
  {
    return query.createWhereClause(ds);
  }


  public XmlWhereClause<T> from(String xmlPath)
  {
    return from(new FilePath(xmlPath));
  }


  public XmlWhereClause<T> from(FilePath xmlPath)
  {
    return fromAttributes(xmlPath);
  }


  public XmlWhereClause<T> fromAttributes(String xmlPath)
  {
    return fromAttributes(new FilePath(xmlPath));
  }


  public XmlWhereClause<T> fromAttributes(FilePath xmlPath)
  {
    return from(new XmlSelectDataSource(xmlPath, true));
  }


  public XmlWhereClause<T> fromElements(String xmlPath)
  {
    return fromElements(new FilePath(xmlPath));
  }


  public XmlWhereClause<T> fromElements(FilePath xmlPath)
  {
    return from(new XmlSelectDataSource(xmlPath, false));
  }

}
