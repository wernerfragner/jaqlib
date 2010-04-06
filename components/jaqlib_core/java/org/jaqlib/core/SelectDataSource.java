package org.jaqlib.core;


public interface SelectDataSource
{

  DsResultSet execute();


  void closeAfterQuery();

}
