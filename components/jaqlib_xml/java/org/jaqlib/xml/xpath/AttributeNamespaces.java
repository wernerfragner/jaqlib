package org.jaqlib.xml.xpath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttributeNamespaces implements Iterable<AttributeNamespace>
{

  private final List<AttributeNamespace> attributeNamespaces = new ArrayList<AttributeNamespace>();


  @Override
  public Iterator<AttributeNamespace> iterator()
  {
    return attributeNamespaces.iterator();
  }


  public void add(AttributeNamespace namespace)
  {
    if (namespace != null)
    {
      attributeNamespaces.add(namespace);
    }
  }


  public boolean isEmpty()
  {
    return attributeNamespaces.isEmpty();
  }


  public String findPrefix(String namespace)
  {
    for (AttributeNamespace ns : this)
    {
      if (ns.getValue().equals(namespace))
      {
        return ns.getPrefix();
      }
    }
    return null;
  }


  public String findNamespace(String prefix)
  {
    for (AttributeNamespace ns : this)
    {
      if (ns.getPrefix().equals(prefix))
      {
        return ns.getValue();
      }
    }
    return null;
  }


  public List<String> findPrefixes(String namespace)
  {
    List<String> result = new ArrayList<String>();
    for (AttributeNamespace ns : this)
    {
      if (ns.getValue().equals(namespace))
      {
        result.add(ns.getPrefix());
      }
    }
    return result;
  }


}
