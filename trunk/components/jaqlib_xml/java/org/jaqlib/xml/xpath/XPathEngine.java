package org.jaqlib.xml.xpath;

import org.jaqlib.util.FilePath;
import org.w3c.dom.NodeList;


public interface XPathEngine
{

  void open(FilePath xmlPath);


  void close();


  NodeList getResults(String expression);

}