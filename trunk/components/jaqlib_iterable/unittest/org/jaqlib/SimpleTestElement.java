package org.jaqlib;


public interface SimpleTestElement extends Comparable<SimpleTestElement>
{

  boolean isMatch();


  Object getObject();


  Integer getCompareValue();

}
