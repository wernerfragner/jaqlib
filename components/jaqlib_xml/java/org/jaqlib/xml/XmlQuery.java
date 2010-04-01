package org.jaqlib.xml;

import java.util.Collection;
import java.util.Map;

import org.jaqlib.core.AbstractQuery;
import org.jaqlib.core.QueryResult;
import org.jaqlib.core.reflect.MethodCallRecorder;

public class XmlQuery<T> extends AbstractQuery<T, XmlDataSource>
{

  private BeanMapping<T> beanMapping;
  private FilePath xmlPath;
  private String xPath;


  public XmlQuery(MethodCallRecorder methodCallRecorder)
  {
    super(methodCallRecorder);
  }


  public XmlFromClause<T> createFromClause(BeanMapping<T> beanMapping)
  {
    this.beanMapping = beanMapping;
    return new XmlFromClause<T>(this);
  }


  public XmlWhereClause<T> createWhereClause(FilePath xmlPath)
  {
    this.xmlPath = xmlPath;
    return new XmlWhereClause<T>(this);
  }


  public QueryResult<T, XmlDataSource> createQueryResult(String xPath)
  {
    this.xPath = xPath;
    return super.createQueryResult();
  }


  protected void setxPath(String xPath)
  {
    this.xPath = xPath;
  }


  @Override
  protected void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    // TODO Auto-generated method stub
  }


  @Override
  protected <KeyType> void addResults(Map<KeyType, T> resultMap)
  {
    // TODO Auto-generated method stub
  }


  @Override
  protected String getResultDefinitionString()
  {
    // TODO Auto-generated method stub
    return null;
  }


}
