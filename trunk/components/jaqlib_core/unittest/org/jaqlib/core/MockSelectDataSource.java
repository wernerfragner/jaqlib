package org.jaqlib.core;

public class MockSelectDataSource implements SelectDataSource
{

  private DsResultSet resultSet = new MockDsResultSet();


  public DsResultSet getResultSet()
  {
    return resultSet;
  }


  public void setResultSet(DsResultSet resultSet)
  {
    this.resultSet = resultSet;
  }


  public DsResultSet execute()
  {
    return resultSet;
  }

}
