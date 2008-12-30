package org.jaqlib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil
{

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
    return new HashMap<K, V>();
  }


  public static <E> List<E> newDefaultList()
  {
    return new ArrayList<E>();
  }


  public static <E> Set<E> newDefaultSet()
  {
    return new HashSet<E>();
  }

}
