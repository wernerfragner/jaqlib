package org.jaqlib.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Collection of {@link XmlNamespace} objects.
 * 
 * @author Werner Fragner
 */
public class XmlNamespaces implements Iterable<XmlNamespace>
{

  private final List<XmlNamespace> namespaces = new ArrayList<XmlNamespace>();


  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<XmlNamespace> iterator()
  {
    return namespaces.iterator();
  }


  /**
   * Adds a new XML namespace to this data source. This namespace is used to
   * lookup XML attribute or element values.
   * 
   * @param prefix the prefix of the namespace. E.g. jaqlib in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   * @param uri the URI of the namespace (can or cannot really exist). E.g.
   *          'http://org.jaqlib/example' in the definition
   *          'xmlns:jaqlib=http://org.jaqlib/example'.
   */
  public void add(String prefix, String uri)
  {
    add(new XmlNamespace(prefix, uri));
  }


  /**
   * Adds the given namespace to this collection.
   * 
   * @param namespace the namespace to add. If <tt>null</tt> is given then no
   *          action is performed.
   */
  public void add(XmlNamespace namespace)
  {
    if (namespace != null)
    {
      namespaces.add(namespace);
    }
  }


  /**
   * See return tag.
   * 
   * @return true if no namespaces are in this collection.
   */
  public boolean isEmpty()
  {
    return namespaces.isEmpty();
  }


  /**
   * Removes all namespace from this collection.
   */
  public void clear()
  {
    namespaces.clear();
  }


  /**
   * Finds the first prefix for the given URI. This method should only be needed
   * internally.
   * 
   * @param uri
   * @return see description.
   */
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


  /**
   * Finds the first URI for the given prefix. This method should only be needed
   * internally.
   * 
   * @param prefix
   * @return see description.
   */
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


  /**
   * Finds all prefixes for the given URI. This method should only be needed
   * internally.
   * 
   * @param uri
   * @return see description.
   */
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
