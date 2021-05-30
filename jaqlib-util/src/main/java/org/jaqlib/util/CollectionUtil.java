package org.jaqlib.util;

import java.util.*;

public class CollectionUtil
{

  public static String toString(Object[] objects, String separator)
  {
    if (objects == null)
    {
      return "";
    }
    return toString(Arrays.asList(objects), separator);
  }


  public static String toString(Iterable<?> iterable, String separator)
  {
    if (iterable == null)
    {
      return "";
    }
    if (separator == null)
    {
      separator = "";
    }

    boolean first = true;
    final StringBuilder sb = new StringBuilder();
    for (Object o : iterable)
    {
      if (!first)
      {
        sb.append(separator);
      }
      else
      {
        first = false;
      }
      sb.append(o);
    }
    return sb.toString();
  }


  public static <K, V> Map<K, V> newDefaultMap()
  {
    return new HashMap<>();
  }


  public static <E> List<E> newDefaultList()
  {
    return new ArrayList<>();
  }


  public static <E> Set<E> newDefaultSet()
  {
    return new HashSet<>();
  }


  public static <E> List<E> newList(E... elements)
  {
    List<E> l = newDefaultList();
    Collections.addAll(l, elements);
    return l;
  }

}
