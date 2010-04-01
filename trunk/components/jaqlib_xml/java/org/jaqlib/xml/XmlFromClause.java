package org.jaqlib.xml;


public class XmlFromClause<T>
{

  private final XmlQuery<T> query;


  public XmlFromClause(XmlQuery<T> query)
  {
    this.query = query;
  }


  public XmlWhereClause<T> from(String xmlPath)
  {
    return from(new FilePath(xmlPath));
  }


  public XmlWhereClause<T> from(FilePath xmlPath)
  {
    return query.createWhereClause(new XmlDataSource(xmlPath));
  }

}
