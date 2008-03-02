package org.jaqlib;


public interface SimpleTestItem extends Comparable<SimpleTestItem>
{

  boolean isMatch();


  Object getObject();


  Integer getCompareValue();

}
