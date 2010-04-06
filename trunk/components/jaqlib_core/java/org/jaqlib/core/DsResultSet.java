package org.jaqlib.core;

public interface DsResultSet
{

  Object NO_RESULT = new Object();


  Object getObject(int valueDataType, int valueIndex);


  Object getObject(int valueDataType, String valueLabel);


  boolean next();

}
