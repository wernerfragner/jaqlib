package org.jaqlib.core;

import org.jaqlib.core.bean.FieldMapping;

public interface DsResultSet
{

  Object NO_RESULT = new Object();


  Object getObject(FieldMapping<?> mapping);


  Object getAnynomousObject(FieldMapping<?> mapping);


  boolean next();

}
