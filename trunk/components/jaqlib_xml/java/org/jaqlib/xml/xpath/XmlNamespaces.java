package org.jaqlib.xml.xpath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlNamespaces implements Iterable<XmlNamespace>
{

  private final List<XmlNamespace> namespaces = new ArrayList<XmlNamespace>();


  @Override
  public Iterator<XmlNamespace> iterator()
  {
    return namespaces.iterator();
  }


  public void add(String prefix, String uri)
  {
    add(new XmlNamespace(prefix, uri));
  }


  public void add(XmlNamespace namespace)
  {
    if (namespace != null)
    {
      namespaces.add(namespace);
    }
  }


  public boolean isEmpty()
  {
    return namespaces.isEmpty();
  }


  public void clear()
  {
    namespaces.clear();
  }


  public String findPrefix(String uri)
  {
    for (XmlNamespace ns : this)
    {
      if (ns.getUri().equals(uri))
      {
        return ns.getPrefix();
      }
    }
    return null;
  }


  public String findNamespace(String prefix)
  {
    for (XmlNamespace ns : this)
    {
      if (ns.getPrefix().equals(prefix))
      {
        return ns.getUri();
      }
    }
    return null;
  }


  public List<String> findPrefixes(String uri)
  {
    List<String> result = new ArrayList<String>();
    for (XmlNamespace ns : this)
    {
      if (ns.getUri().equals(uri))
      {
        result.add(ns.getPrefix());
      }
    }
    return result;
  }


}
