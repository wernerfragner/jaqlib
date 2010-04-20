package org.jaqlib.xml.xpath;

import org.jaqlib.util.Resource;
import org.w3c.dom.NodeList;


public interface XPathEngine
{

  void open(Resource xmlPath);


  void close();


  NodeList getResults(String expression);

}